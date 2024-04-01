package by.sapra.tasktrecker.task.service;

import by.sapra.tasktrecker.config.AbstractDataTest;
import by.sapra.tasktrecker.task.model.TaskModel;
import by.sapra.tasktrecker.task.model.UserLinks;
import by.sapra.tasktrecker.task.model.storade.TaskUserStorage;
import by.sapra.tasktrecker.testUtil.TestDataBuilder;
import by.sapra.tasktrecker.user.model.UserModel;
import org.jetbrains.annotations.NotNull;
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
        TestDataBuilder<UserModel> author = createAuthor();
        TestDataBuilder<UserModel> assignee = createAssignee();
        List<TestDataBuilder<UserModel>> observers = createObservers();

        TaskModel expected = saveTask(author, assignee, observers);
        UserLinks userLinks = createLinks(author, assignee, observers);

        when(taskUserStorage.getUserLinks(any()))
                .thenReturn(userLinks);

        TaskModel actual = service.getById(expected.getId()).block();

        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(expected, actual);
        });
    }

    @Test
    void whenAuthorIsNotExist_thenReturnTaskWithoutAuthor() throws Exception {
        TestDataBuilder<UserModel> author = aUser();
        TestDataBuilder<UserModel> assignee = createAssignee();
        List<TestDataBuilder<UserModel>> observers = createObservers();

        TaskModel expected = saveTask(author, assignee, observers);

        UserLinks links = createLinks(() -> null, assignee, observers);
        when(taskUserStorage.getUserLinks(any()))
                .thenReturn(links);

        TaskModel actual = service.getById(expected.getId()).block();

        assertAll(() -> {
            assertNotNull(actual);
            assertNull(actual.getAuthor());
        });
    }

    private TestDataBuilder<UserModel> createAuthor() {
        return testDbFacade.persistedOnce(aUser());
    }

    private TestDataBuilder<UserModel> createAssignee() {
        return testDbFacade.persistedOnce(aUser());
    }

    @NotNull
    private List<TestDataBuilder<UserModel>> createObservers() {
        return List.of(
                testDbFacade.persistedOnce(aUser()),
                testDbFacade.persistedOnce(aUser()),
                testDbFacade.persistedOnce(aUser())
        );
    }

    private TaskModel saveTask(TestDataBuilder<UserModel> author, TestDataBuilder<UserModel> assignee, List<TestDataBuilder<UserModel>> observers) {
        return testDbFacade.save(
                aTask()
                        .withAuthor(author)
                        .withAssignee(assignee)
                        .withObservers(observers)
        );
    }

    private static UserLinks createLinks(TestDataBuilder<UserModel> author, TestDataBuilder<UserModel> assignee, List<TestDataBuilder<UserModel>> observers) {
        UserModel authorToSave = author.build();
        return UserLinks.builder()
                .author(authorToSave == null ? Mono.empty() : Mono.just(authorToSave))
                .assignee(Mono.just(assignee.build()))
                .observers(observers.stream().map(TestDataBuilder::build).map(Mono::just).toList())
                .build();
    }
}