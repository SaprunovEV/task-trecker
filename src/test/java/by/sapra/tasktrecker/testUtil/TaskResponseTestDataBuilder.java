package by.sapra.tasktrecker.testUtil;

import by.sapra.tasktrecker.task.web.v1.model.TaskResponse;
import by.sapra.tasktrecker.user.web.v1.model.UserResponse;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskResponseTestDataBuilder implements TestDataBuilder<TaskResponse> {
    private String id = UUID.randomUUID().toString();
    private String name = "test_name";
    private String description = "test_description";
    private Instant createAt = Instant.now();
    private Instant updateAt = Instant.now();
    private TestDataBuilder<UserResponse> author = UserResponse::new;
    private TestDataBuilder<UserResponse> assignee = UserResponse::new;
    private List<TestDataBuilder<UserResponse>> observers = new ArrayList<>();

    private TaskResponseTestDataBuilder() {
    }

    private TaskResponseTestDataBuilder(String id, String name, String description, Instant createAt, Instant updateAt, TestDataBuilder<UserResponse> author, TestDataBuilder<UserResponse> assignee, List<TestDataBuilder<UserResponse>> observer4s) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.author = author;
        this.assignee = assignee;
        this.observers = observer4s;
    }

    public static TaskResponseTestDataBuilder aTaskResponse() {
        return new TaskResponseTestDataBuilder();
    }

    public TaskResponseTestDataBuilder withId(String id) {
        return id == this.id ? this : new TaskResponseTestDataBuilder(
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

    public TaskResponseTestDataBuilder withName(String name) {
        return name == this.name ? this : new TaskResponseTestDataBuilder(
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

    public TaskResponseTestDataBuilder withDescription(String description) {
        return description == this.description ? this : new TaskResponseTestDataBuilder(
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

    public TaskResponseTestDataBuilder withCreateAt(Instant createAt) {
        return createAt == this.createAt ? this : new TaskResponseTestDataBuilder(
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

    public TaskResponseTestDataBuilder withUpdateAt(Instant updateAt) {
        return updateAt == this.updateAt ? this : new TaskResponseTestDataBuilder(
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

    public TaskResponseTestDataBuilder withAuthor(TestDataBuilder<UserResponse> author) {
        return author == this.author ? this : new TaskResponseTestDataBuilder(
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

    public TaskResponseTestDataBuilder withAssignee(TestDataBuilder<UserResponse> assignee) {
        return assignee == this.assignee ? this : new TaskResponseTestDataBuilder(
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

    public TaskResponseTestDataBuilder withObservers(List<TestDataBuilder<UserResponse>> observers) {
        return observers == this.observers ? this : new TaskResponseTestDataBuilder(
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
    public TaskResponse build() {
        TaskResponse result = new TaskResponse();
        result.setId(id);
        result.setName(name);
        result.setDescription(description);
        result.setCreateAt(createAt);
        result.setUpdateAt(updateAt);
        result.setAuthor(author.build());
        result.setAssignee(assignee.build());
        result.setObservers(observers.stream().map(TestDataBuilder::build).collect(Collectors.toSet()));
        return result;
    }
}
