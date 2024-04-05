package by.sapra.tasktrecker.testUtil;

import by.sapra.tasktrecker.task.model.TaskModel;
import by.sapra.tasktrecker.user.model.UserModel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskModelTestDataBuilder implements TestDataBuilder<TaskModel> {
    private String id = UUID.randomUUID().toString();
    private String name = "test_name";
    private String description = "test_description";
    private Instant createAt = Instant.now();
    private Instant updateAt = Instant.now();
    private TestDataBuilder<UserModel> author = UserModel::new;
    private TestDataBuilder<UserModel> assignee = UserModel::new;
    private List<TestDataBuilder<UserModel>> observers = new ArrayList<>();

    private TaskModelTestDataBuilder() {
    }

    private TaskModelTestDataBuilder(String id, String name, String description, Instant createAt, Instant updateAt, TestDataBuilder<UserModel> author, TestDataBuilder<UserModel> assignee, List<TestDataBuilder<UserModel>> observer4s) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.author = author;
        this.assignee = assignee;
        this.observers = observer4s;
    }

    public static TaskModelTestDataBuilder aTask() {
        return new TaskModelTestDataBuilder();
    }

    public TaskModelTestDataBuilder withId(String id) {
        return id == this.id ? this : new TaskModelTestDataBuilder(
                id,
                name,
                description,
                createAt,
                updateAt,
                author,
                assignee,
                observers
        );
    }

    public TaskModelTestDataBuilder withName(String name) {
        return name == this.name ? this : new TaskModelTestDataBuilder(
                id,
                name,
                description,
                createAt,
                updateAt,
                author,
                assignee,
                observers
        );
    }

    public TaskModelTestDataBuilder withDescription(String description) {
        return description == this.description ? this : new TaskModelTestDataBuilder(
                id,
                name,
                description,
                createAt,
                updateAt,
                author,
                assignee,
                observers
        );
    }

    public TaskModelTestDataBuilder withCreateAt(Instant createAt) {
        return createAt == this.createAt ? this : new TaskModelTestDataBuilder(
                id,
                name,
                description,
                createAt,
                updateAt,
                author,
                assignee,
                observers
        );
    }

    public TaskModelTestDataBuilder withUpdateAt(Instant updateAt) {
        return updateAt == this.updateAt ? this : new TaskModelTestDataBuilder(
                id,
                name,
                description,
                createAt,
                updateAt,
                author,
                assignee,
                observers
        );
    }

    public TaskModelTestDataBuilder withAuthor(TestDataBuilder<UserModel> author) {
        return author == this.author ? this : new TaskModelTestDataBuilder(
                id,
                name,
                description,
                createAt,
                updateAt,
                author,
                assignee,
                observers                
        );
    }

    public TaskModelTestDataBuilder withAssignee(TestDataBuilder<UserModel> assignee) {
        return assignee == this.assignee ? this : new TaskModelTestDataBuilder(
                id,
                name,
                description,
                createAt,
                updateAt,
                author,
                assignee,
                observers
        );
    }

    public TaskModelTestDataBuilder withObservers(List<TestDataBuilder<UserModel>> observers) {
        return observers == this.observers ? this : new TaskModelTestDataBuilder(
                id,
                name,
                description,
                createAt,
                updateAt,
                author,
                assignee,
                observers
        );
    }

    @Override
    public TaskModel build() {
        TaskModel result = new TaskModel();
        result.setId(id);
        result.setName(name);
        result.setDescription(description);
        result.setCreateAt(createAt);
        result.setUpdateAt(updateAt);
        UserModel author = this.author.build();
        result.setAuthor(author);
        result.setAuthorId(author.getId());
        UserModel assignee = this.assignee.build();
        result.setAssignee(assignee);
        result.setAssigneeId(assignee.getId());
        Set<UserModel> observers = this.observers.stream().map(TestDataBuilder::build).collect(Collectors.toSet());
        result.setObservers(observers);
        result.setObserverIds(observers.stream().map(UserModel::getId).collect(Collectors.toSet()));
        return result;
    }
}
