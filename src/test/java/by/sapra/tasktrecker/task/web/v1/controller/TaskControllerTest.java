package by.sapra.tasktrecker.task.web.v1.controller;

import by.sapra.tasktrecker.task.model.TaskModel;
import by.sapra.tasktrecker.task.service.TaskService;
import by.sapra.tasktrecker.task.web.v1.mapper.TaskResponseMapper;
import by.sapra.tasktrecker.task.web.v1.model.TaskResponse;
import by.sapra.tasktrecker.task.web.v1.model.TaskUploadRequest;
import by.sapra.tasktrecker.task.web.v1.model.enums.TaskStatus;
import by.sapra.tasktrecker.testUtil.TaskModelTestDataBuilder;
import by.sapra.tasktrecker.testUtil.TaskResponseTestDataBuilder;
import by.sapra.tasktrecker.testUtil.UserResponseTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static by.sapra.tasktrecker.testUtil.TaskModelTestDataBuilder.aTask;
import static by.sapra.tasktrecker.testUtil.TaskResponseTestDataBuilder.aTaskResponse;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = TaskController.class)
class TaskControllerTest {
    private final String uri = "/api/v1/tasks";
    @Autowired
    WebTestClient client;
    @MockBean
    private TaskService service;
    @MockBean
    private TaskResponseMapper mapper;

    @Test
    void whenFindAll_thenReturnStatusOk() throws Exception {
        UserResponseTestDataBuilder userBuilder = UserResponseTestDataBuilder.aUserResponse();
        Flux<TaskResponse> expected = Flux.just(
                createTask(userBuilder),
                createTask(userBuilder),
                createTask(userBuilder)
        );

        Flux<TaskModel> fluxTaskModel = Flux.just(
                aTask().build(),
                aTask().build(),
                aTask().build(),
                aTask().build()
        );

        when(service.getAll()).thenReturn(fluxTaskModel);

        when(mapper.fluxTaskModelToFluxTaskResponse(fluxTaskModel))
                .thenReturn(expected);

        client.get().uri(uri)
                .exchange().expectStatus().isOk()
                .expectBodyList(TaskResponse.class)
                .hasSize(3);

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).fluxTaskModelToFluxTaskResponse(fluxTaskModel);
    }

    @Test
    void whenGetAll_andDatabaseIsEmpty_thenReturnEmptyList() throws Exception {
        when(mapper.fluxTaskModelToFluxTaskResponse(any()))
                .thenReturn(Flux.empty());

        client.get().uri(uri)
                .exchange().expectStatus().isOk()
                .expectBodyList(TaskResponse.class)
                .hasSize(0);

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).fluxTaskModelToFluxTaskResponse(any());
    }

    @Test
    void whenGetById_thenReturnTaskById() throws Exception {
        String id = UUID.randomUUID().toString();
        TaskResponse response = aTaskResponse().withId(id).build();

        Mono<TaskModel> monoModel = Mono.just(aTask().withId(id).build());
        when(service.getById(id)).thenReturn(monoModel);

        when(mapper.monoTaskModelToMonoTaskResponse(monoModel))
                .thenReturn(Mono.just(response));

        client.get().uri(uri + "/{id}", id)
                .exchange().expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .isEqualTo(response);

        verify(service, times(1)).getById(id);
        verify(mapper, times(1)).monoTaskModelToMonoTaskResponse(monoModel);
    }

    @Test
    void whenCreateNewTask_thenReturnCreatedTask() throws Exception {
        TaskUploadRequest request = new TaskUploadRequest(
                "name",
                "description",
                TaskStatus.TODO,
                "author_id",
                "assigneeId"
        );

        Mono<TaskModel> model = Mono.just(aTask().build());
        when(mapper.monoRequestToModel(request))
                .thenReturn(model);

        Mono<TaskModel> model2response = Mono.just(aTask().build());
        when(service.saveNewTask(model))
                .thenReturn(model2response);

        TaskResponse expected = aTaskResponse().build();
        Mono<TaskResponse> response = Mono.just(expected);
        when(mapper.monoTaskModelToMonoTaskResponse(model2response))
                .thenReturn(response);

        client.post().uri(uri)
                .body(Mono.just(request), TaskUploadRequest.class)
                .exchange().expectStatus().isCreated()
                .expectBody(TaskResponse.class)
                .isEqualTo(expected);
    }

    private static TaskResponse createTask(UserResponseTestDataBuilder userBuilder) {
        return aTaskResponse()
                .withAuthor(userBuilder)
                .withAssignee(userBuilder)
                .withObservers(List.of(userBuilder))
                .build();
    }
}