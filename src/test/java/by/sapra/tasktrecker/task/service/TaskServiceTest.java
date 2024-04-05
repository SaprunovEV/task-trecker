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
import static org.mockito.Mockito.*;

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

    @Test
    void whenAuthorIsNotExist_thenReturnTaskWithoutAssignee() throws Exception {
        TestDataBuilder<UserModel> author = createAuthor();
        TestDataBuilder<UserModel> assignee = aUser();
        List<TestDataBuilder<UserModel>> observers = createObservers();

        TaskModel expected = saveTask(author, assignee, observers);

        UserLinks links = createLinks(author, () -> null, observers);
        when(taskUserStorage.getUserLinks(any()))
                .thenReturn(links);

        TaskModel actual = service.getById(expected.getId()).block();

        assertAll(() -> {
            assertNotNull(actual);
            assertNull(actual.getAssignee());
        });
    }

    @Test
    void whenGetAll_thenReturnAllTasks() throws Exception {
        TestDataBuilder<UserModel> author1 = createAuthor();
        TestDataBuilder<UserModel> assignee1 = createAssignee();
        List<TestDataBuilder<UserModel>> observers1 = createObservers();

        List<TaskModel> expected = List.of(
                saveTask(author1, assignee1, observers1),
                saveTask(author1, assignee1, observers1),
                saveTask(author1, assignee1, observers1),
                saveTask(author1, assignee1, observers1)
        );

        UserLinks links = createLinks(author1, assignee1, observers1);
        when(taskUserStorage.getUserLinks(any()))
                .thenReturn(links);

        List<TaskModel> actual = service.getAll().collectList().block();

        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(expected.size(), actual.size());
        });

        verify(taskUserStorage, times(expected.size())).getUserLinks(any());
    }

    @Test
    void whenSaveNewTask_thenReturnSavedTask() throws Exception {
        TestDataBuilder<UserModel> author = createAuthor();
        TestDataBuilder<UserModel> assignee = createAssignee();
        List<TestDataBuilder<UserModel>> observers = createObservers();

        TaskModel model = aTask()
                .withAssignee(assignee)
                .withAuthor(author)
                .withObservers(observers)
                .withId(null)
                .build();

        String prevId = model.getId();

        when(taskUserStorage.getUserLinks(any()))
                .thenReturn(createLinks(author,assignee, observers));

        Mono<TaskModel> actual = service.saveNewTask(model);

        assertAll(() -> {
            assertNotNull(actual);
            assertNotEquals(prevId, actual.block().getId());
            assertNotNull(testDbFacade.find(actual.block().getId(), TaskModel.class));
        });
    }

    @Test
    void whenUpdateTaskWithNameAndDescription_thenReturnUpdatedTask() throws Exception {
        TestDataBuilder<UserModel> author = createAuthor();
        TestDataBuilder<UserModel> assignee = createAssignee();
        List<TestDataBuilder<UserModel>> observers = createObservers();

        TaskModel savedModel = saveTask(author, assignee, observers);
        String id2update = savedModel.getId();

        TaskModel model2update = aTask()
                .withObservers(observers)
                .withAuthor(author)
                .withAssignee(assignee)
                .withName("new_name")
                .withDescription("new_description")
                .build();

        when(taskUserStorage.getUserLinks(any()))
                .thenReturn(createLinks(author,assignee, observers));

        Mono<TaskModel> actual = service.updateTask(id2update, model2update);

        assertAll(() -> {
            assertNotNull(actual);
            TaskModel task = actual.block();
            assertTaskModel(id2update, model2update, task, "returned task");
            task = testDbFacade.find(id2update, TaskModel.class);
            assertTaskModel(id2update, model2update, task, "database task");
        });
    }

    @Test
    void whenUpdateTaskWithNewAssignee_thenReturnUpdatedTask() throws Exception {
        TestDataBuilder<UserModel> author = createAuthor();
        TestDataBuilder<UserModel> assignee = createAssignee();
        List<TestDataBuilder<UserModel>> observers = createObservers();

        TaskModel savedModel = saveTask(author, assignee, observers);
        String id2update = savedModel.getId();

        TestDataBuilder<UserModel> newAssignee = createAssignee();
        TaskModel model2update = aTask()
                .withObservers(observers)
                .withAuthor(author)
                .withAssignee(newAssignee)
                .withName("new_name")
                .withDescription("new_description")
                .build();

        when(taskUserStorage.getUserLinks(any()))
                .thenReturn(createLinks(author,newAssignee, observers));

        Mono<TaskModel> actual = service.updateTask(id2update, model2update);

        assertAll(() -> {
            assertNotNull(actual);
            TaskModel task = actual.block();
            assertTaskModel(id2update, model2update, task, "returned task");
            task = testDbFacade.find(id2update, TaskModel.class);
            assertTaskModel(id2update, model2update, task, "database task");
        });
    }

    private static void assertTaskModel(String id2update, TaskModel model2update, TaskModel task, String message) {
        assertEquals(model2update.getName(), task.getName(), message);
        assertEquals(id2update, task.getId(), message);
        assertEquals(model2update.getDescription(), task.getDescription(), message);
        assertEquals(model2update.getAssigneeId(), task.getAssigneeId());
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
        UserModel assigneeToSave = assignee.build();
        return UserLinks.builder()
                .author(authorToSave == null ? Mono.empty() : Mono.just(authorToSave))
                .assignee(assigneeToSave == null ? Mono.empty() : Mono.just(assigneeToSave))
                .observers(observers.stream().map(TestDataBuilder::build).map(Mono::just).toList())
                .build();
    }
}