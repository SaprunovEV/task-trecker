package by.sapra.tasktrecker.task.web.v1.controller;

import by.sapra.tasktrecker.task.model.TaskModel;
import by.sapra.tasktrecker.task.service.TaskService;
import by.sapra.tasktrecker.task.web.v1.mapper.TaskResponseMapper;
import by.sapra.tasktrecker.task.web.v1.model.TaskResponse;
import by.sapra.tasktrecker.task.web.v1.model.TaskUploadRequest;
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
import static by.sapra.tasktrecker.testUtil.TaskUploadRequestTestDataBuilder.aRequest;
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
        TaskUploadRequest request = aRequest().build();

        TaskModel model = aTask().build();
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

    @Test
    void whenUpdateTask_thenReturnUpdatedTask() throws Exception {
        TaskUploadRequest request = aRequest().build();
        String taskId = UUID.randomUUID().toString();

        TaskModel task2update = aTask().build();
        when(mapper.monoRequestToModel(request))
                .thenReturn(task2update);

        Mono<TaskModel> task2response = Mono.just(aTask().withId(taskId).build());
        when(service.updateTask(taskId, task2update))
                .thenReturn(task2response);

        TaskResponse expected = aTaskResponse().withId(taskId).build();
        when(mapper.monoTaskModelToMonoTaskResponse(task2response))
                .thenReturn(Mono.just(expected));

        client.put().uri(uri + "/{id}", taskId)
                .body(Mono.just(request), TaskUploadRequest.class)
                .exchange().expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .isEqualTo(expected);

        verify(mapper, times(1)).monoRequestToModel(request);
        verify(service, times(1)).updateTask(taskId, task2update);
        verify(mapper, times(1)).monoTaskModelToMonoTaskResponse(task2response);
    }

    @Test
    void whenAddObservers_thenReturnTaskWithNewObservers() throws Exception {
        String taskId = UUID.randomUUID().toString();
        String observerId = UUID.randomUUID().toString();

        Mono<TaskModel> task2response = Mono.just(aTask().withId(taskId).build());
        when(service.addObserver(taskId, observerId))
                .thenReturn(task2response);

        TaskResponse expected = aTaskResponse().withId(taskId).build();
        when(mapper.monoTaskModelToMonoTaskResponse(task2response))
                .thenReturn(Mono.just(expected));

        client.put().uri(uri + "/{taskId}/observers/add/{observerId}", taskId, observerId)
                .exchange().expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .isEqualTo(expected);

        verify(service, times(1)).addObserver(taskId, observerId);
        verify(mapper, times(1)).monoTaskModelToMonoTaskResponse(task2response);
    }

    @Test
    void whenDeleteById_thenReturnNoContent() throws Exception {
        String id = UUID.randomUUID().toString();

        when(service.deleteById(id)).thenReturn(Mono.empty());

        client.delete().uri(uri + "/{id}", id)
                .exchange().expectStatus().isNoContent();

        verify(service, times(1)).deleteById(id);
    }

    private static TaskResponse createTask(UserResponseTestDataBuilder userBuilder) {
        return aTaskResponse()
                .withAuthor(userBuilder)
                .withAssignee(userBuilder)
                .withObservers(List.of(userBuilder))
                .build();
    }
}