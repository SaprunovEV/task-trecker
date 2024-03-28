package by.sapra.tasktrecker.task.web.v1.controller;

import by.sapra.tasktrecker.task.model.TaskModel;
import by.sapra.tasktrecker.task.service.TaskService;
import by.sapra.tasktrecker.task.web.v1.mapper.TaskResponseMapper;
import by.sapra.tasktrecker.task.web.v1.model.TaskResponse;
import by.sapra.tasktrecker.testUtil.UserResponseTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

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

    private static TaskResponse createTask(UserResponseTestDataBuilder userBuilder) {
        return aTaskResponse()
                .withAuthor(userBuilder)
                .withAssignee(userBuilder)
                .withObservers(List.of(userBuilder))
                .build();
    }
}