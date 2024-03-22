package by.sapra.tasktrecker.user.web.v1.controller;

import by.sapra.tasktrecker.user.service.UserService;
import by.sapra.tasktrecker.user.service.model.UserModel;
import by.sapra.tasktrecker.user.web.v1.mappers.ResponseMapper;
import by.sapra.tasktrecker.user.web.v1.model.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static org.mockito.Mockito.when;


@WebFluxTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    WebTestClient client;
    @MockBean
    private UserService service;
    @MockBean
    private ResponseMapper responseMapper;

    @Test
    void whenGetAllUsers_thenReturnAllUsersResponses() throws Exception {
        Flux<UserResponse> expected = Flux.just(
                new UserResponse(UUID.randomUUID().toString(), "user name 1", "email1@test.test"),
                new UserResponse(UUID.randomUUID().toString(), "user name 2", "email2@test.test"),
                new UserResponse(UUID.randomUUID().toString(), "user name 3", "email3@test.test")
        );

        Flux<UserModel> list = Flux.just(
                new UserModel(UUID.randomUUID().toString(), "user name 1", "email1@test.test")
        );
        when(service.getAll()).thenReturn(list);

        when(responseMapper.userModelListToUserResponsList(list))
                .thenReturn(expected);

        client.get().uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .hasSize(3);
    }
}