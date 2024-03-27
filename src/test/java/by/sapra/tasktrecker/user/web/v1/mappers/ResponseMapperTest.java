package by.sapra.tasktrecker.user.web.v1.mappers;

import by.sapra.tasktrecker.user.model.UserModel;
import by.sapra.tasktrecker.user.web.v1.model.UpsertUserRequest;
import by.sapra.tasktrecker.user.web.v1.model.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static by.sapra.tasktrecker.testUtil.UserTestDataBuilder.aUser;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ResponseMapperConf.class)
class ResponseMapperTest {

    @Autowired
    ResponseMapper mapper;

    @Test
    void mapperShouldBeCreated() throws Exception {
        assertNotNull(mapper);
    }

    @Test
    void whenRequestMapToUser_thenReturnCorrectUser() throws Exception {
        UpsertUserRequest expected = new UpsertUserRequest();
        expected.setEmail("test_email");
        expected.setName("test_name");

        UserModel actual = mapper.requestToModel(expected);

        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getEmail(), actual.getEmail());
        });
    }

    @Test
    void whenMonoUserMapToMonoResponse_thenReturnCorrectMonoResponse() throws Exception {
        UserModel expected = aUser().build();
        Mono<UserModel> mono = Mono.just(expected);

        Mono<UserResponse> actual = mapper.monoModelToMonoResponse(mono);

        assertAll(() -> {
            assertNotNull(actual);
            assertTrue(actual.hasElement().block());
            UserResponse response = actual.block();
            assertEquals(expected.getEmail(), response.getEmail());
            assertEquals(expected.getName(), response.getName());
            assertEquals(expected.getId(), response.getId());
        });
    }

    @Test
    void whenFluxOfUserMapToFluxUserResponse_thenReturnCorrectFluxOfUserResponse() throws Exception {
        Flux<UserModel> expected = Flux.just(
                aUser().withName("name 1").build(),
                aUser().withName("name 2").build(),
                aUser().withName("name 3").build(),
                aUser().withName("name 4").build(),
                aUser().withName("name 5").build(),
                aUser().withName("name 6").build()
        );

        Flux<UserResponse> actual = mapper.userModelListToUserResponseList(expected);

        assertAll(() -> {
            assertNotNull(actual);
            List<UserResponse> listActual = actual.toStream().toList();
            List<UserModel> listExpected = expected.toStream().toList();

            assertEquals(listExpected.size(), listActual.size());

            for (int i = 0; i < listExpected.size(); i++) {
                assertEquals(listExpected.get(i).getName(), listActual.get(i).getName());
                assertEquals(listExpected.get(i).getEmail(), listActual.get(i).getEmail());
                assertEquals(listExpected.get(i).getId(), listActual.get(i).getId());
            }
        });
    }
}