package by.sapra.tasktrecker.user.web.v1.controller;

import by.sapra.tasktrecker.user.service.UserService;
import by.sapra.tasktrecker.user.service.model.UserModel;
import by.sapra.tasktrecker.user.web.v1.mappers.ResponseMapper;
import by.sapra.tasktrecker.user.web.v1.model.UpsertUserRequest;
import by.sapra.tasktrecker.user.web.v1.model.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

        when(responseMapper.userModelListToUserResponseList(list))
                .thenReturn(expected);

        client.get().uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .hasSize(3);
    }

    @Test
    void whenGetById_thenReturnUserResponseById() throws Exception {
        String id = UUID.randomUUID().toString();
        UserResponse expected = new UserResponse(id, "user name 1", "email1@test.test");
        Mono<UserResponse> expectedMono = Mono.just(expected);

        Mono<UserModel> model = Mono.just(new UserModel(id, "user name 1", "email1@test.test"));

        when(service.getById(id)).thenReturn(model);
        when(responseMapper.monoModelToMonoResponse(model))
                .thenReturn(expectedMono);

        client.get().uri("/api/v1/users/{id}", id)
                .exchange().expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(expected);
    }

    @Test
    void whenCreateNewUser_thenReturnCreationUser() throws Exception {
        String name = "user name 1";
        String email = "email1@test.test";
        UpsertUserRequest request = new UpsertUserRequest(name, email);

        UserModel model2create = new UserModel();
        when(responseMapper.requestToModel(request))
                .thenReturn(model2create);

        Mono<UserModel> monoUser = Mono.just(new UserModel());
        when(service.createNewUser(model2create))
                .thenReturn(monoUser);

        UserResponse expected = new UserResponse(UUID.randomUUID().toString(), name, email);
        when(responseMapper.monoModelToMonoResponse(monoUser))
                .thenReturn(Mono.just(expected));

        client.post().uri("/api/v1/users")
                .body(Mono.just(request), UpsertUserRequest.class)
                .exchange().expectStatus().isCreated()
                .expectBody(UserResponse.class)
                .isEqualTo(expected);
    }
}