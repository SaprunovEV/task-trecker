package by.sapra.tasktrecker.user.service;

import by.sapra.tasktrecker.config.AbstractDataTest;
import by.sapra.tasktrecker.user.model.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;

import java.util.List;
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
}