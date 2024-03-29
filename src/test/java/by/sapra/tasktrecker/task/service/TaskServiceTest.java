package by.sapra.tasktrecker.task.service;

import by.sapra.tasktrecker.config.AbstractDataTest;
import by.sapra.tasktrecker.task.model.TaskModel;
import by.sapra.tasktrecker.task.model.UserLinks;
import by.sapra.tasktrecker.task.model.storade.TaskUserStorage;
import by.sapra.tasktrecker.testUtil.TestDataBuilder;
import by.sapra.tasktrecker.user.model.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;

import java.util.List;

import static by.sapra.tasktrecker.testUtil.TaskModelTestDataBuilder.aTask;
import static by.sapra.tasktrecker.testUtil.UserTestDataBuilder.aUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataMongoTest
@ContextConfiguration(classes = TaskServiceConf.class)
class TaskServiceTest extends AbstractDataTest {
    @Autowired
    TaskService service;
    @MockBean
    TaskUserStorage taskUserStorage;

    @Test
    void whenFindById_thenReturnCorrectTask() throws Exception {
        TestDataBuilder<UserModel> author = testDbFacade.persistedOnce(aUser());
        TestDataBuilder<UserModel> assignee = testDbFacade.persistedOnce(aUser());
        List<TestDataBuilder<UserModel>> observers = List.of(
                testDbFacade.persistedOnce(aUser()),
                testDbFacade.persistedOnce(aUser()),
                testDbFacade.persistedOnce(aUser())
        );
        TaskModel expected = testDbFacade.save(
                aTask()
                        .withAuthor(author)
                        .withAssignee(assignee)
                        .withObservers(observers)
        );

        UserLinks userLinks = UserLinks.builder()
                .author(Mono.just(author.build()))
                .assignee(Mono.just(assignee.build()))
                .observers(observers.stream().map(TestDataBuilder::build).map(Mono::just).toList())
                .build();

        when(taskUserStorage.getUserLinks(any()))
                .thenReturn(userLinks);

        TaskModel actual = service.getById(expected.getId()).block();

        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(expected, actual);
        });
    }
}