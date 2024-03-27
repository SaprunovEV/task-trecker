package by.sapra.tasktrecker.user.service;

import by.sapra.tasktrecker.config.AbstractDataTest;
import by.sapra.tasktrecker.user.model.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static by.sapra.tasktrecker.testUtil.UserTestDataBuilder.aUser;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ContextConfiguration(classes = UserServiceConf.class)
class UserServiceTest extends AbstractDataTest {
    @Autowired
    UserService service;
    @Test
    void whenFindAll_thenReturnAllUsers() throws Exception {
        List<String> expected = Stream.of(
                testDbFacade.save(aUser().withName("name1")),
                testDbFacade.save(aUser().withName("name2")),
                testDbFacade.save(aUser().withName("name2")),
                testDbFacade.save(aUser().withName("name3"))
        ).map(UserModel::getName).sorted(String::compareTo).toList();

        List<String> actual = service.getAll().toStream().map(UserModel::getName).sorted(String::compareTo).toList();

        assertAll(() -> {
            assertNotNull(actual);
            assertFalse(actual.isEmpty());
            assertEquals(expected.size(), actual.size());
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i), actual.get(i));
            }
        });
    }

    @Test
    void whenFindAll_andDatabaseIsEmpty_thenReturnEmptyFlux() throws Exception {
        Flux<UserModel> actual = service.getAll();

        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(0, actual.toStream().count());
        });
    }

    @Test
    void whenFindById_andDatabaseNotFound_thenReturnEmpty() throws Exception {
        Mono<UserModel> actual = service.getById(UUID.randomUUID().toString());

        assertAll(() -> {
            assertNotNull(actual);
            assertNotEquals(Boolean.TRUE, actual.hasElement().block());
        });
    }

    @Test
    void whenGetById_thenReturnUserWithId() throws Exception {
        List<UserModel> list = List.of(
                testDbFacade.save(aUser().withName("name1")),
                testDbFacade.save(aUser()),
                testDbFacade.save(aUser()),
                testDbFacade.save(aUser()),
                testDbFacade.save(aUser()),
                testDbFacade.save(aUser())
        );

        Mono<UserModel> actual = service.getById(list.get(0).getId());

        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(list.get(0), actual.block());
        });
    }

    @Test
    void whenSaveNewUser_thenUserTakeId_andSavedToDatabase() throws Exception {
        UserModel expected = aUser().build();

        Mono<UserModel> result = service.createNewUser(expected);

        UserModel block = result.block();
        expected.setId(block.getId());
        UserModel actual = testDbFacade.find(block.getId(), UserModel.class);

        assertEquals(expected, actual);
    }

    @Test
    void whenUpdateUser_thenReturnUpdatedUser_andDatabaseHaveNewData() throws Exception {
        UserModel savedUser = testDbFacade.save(aUser());

        UserModel user2update = aUser().withName("new_name").withEmail("new_email").build();

        UserModel actual = service.updateUser(savedUser.getId(), user2update).block();

        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(actual.getId(), savedUser.getId());
            assertEquals(user2update.getName(), actual.getName());
            assertEquals(user2update.getEmail(), actual.getEmail());
            UserModel databaseUser = testDbFacade.find(savedUser.getId(), UserModel.class);
            assertNotNull(databaseUser);
            assertEquals(actual, databaseUser);
        });
    }

    @Test
    void whenUpdateNonExistentUser_thenThrowError() throws Exception {
        Mono<UserModel> actual = service.updateUser(UUID.randomUUID().toString(), aUser().build());

        assertAll(() -> {
            assertNotNull(actual);
            assertFalse(actual.hasElement().block().booleanValue());
        });
    }
}