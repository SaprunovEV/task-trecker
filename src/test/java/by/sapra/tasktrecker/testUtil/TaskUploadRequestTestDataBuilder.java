package by.sapra.tasktrecker.testUtil;

import by.sapra.tasktrecker.task.web.v1.model.TaskUploadRequest;
import by.sapra.tasktrecker.task.web.v1.model.enums.TaskStatus;

import java.util.UUID;

public class TaskUploadRequestTestDataBuilder implements TestDataBuilder<TaskUploadRequest>{

    private String name = "test_name";
    private String description = "test_description";
    private TaskStatus taskStatus = TaskStatus.TODO;
    private String authorId = UUID.randomUUID().toString();
    private String assigneeId = UUID.randomUUID().toString();

    private TaskUploadRequestTestDataBuilder() {
    }

    private TaskUploadRequestTestDataBuilder(String name, String description, TaskStatus taskStatus, String authorId, String assigneeId) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.authorId = authorId;
        this.assigneeId = assigneeId;
    }

    public static TaskUploadRequestTestDataBuilder aRequest() {
        return new TaskUploadRequestTestDataBuilder();
    }

    public TaskUploadRequestTestDataBuilder withName(String name) {
        return name == this.name ? this : new TaskUploadRequestTestDataBuilder(name, description, taskStatus, authorId, assigneeId);
    }

    public TaskUploadRequestTestDataBuilder withDescription(String description) {
        return description == this.description ? this : new TaskUploadRequestTestDataBuilder(name, description, taskStatus, authorId, assigneeId);
    }

    public TaskUploadRequestTestDataBuilder withAuthorId(String authorId) {
        return authorId == this.authorId ? this : new TaskUploadRequestTestDataBuilder(name, description, taskStatus, authorId, assigneeId);
    }

    public TaskUploadRequestTestDataBuilder withAssigneeId(String assigneeId) {
        return assigneeId == this.assigneeId ? this : new TaskUploadRequestTestDataBuilder(name, description, taskStatus, authorId, assigneeId);
    }

    @Override
    public TaskUploadRequest build() {
        TaskUploadRequest request = new TaskUploadRequest();

        request.setName(name);
        request.setDescription(description);
        request.setTaskStatus(taskStatus);
        request.setAuthorId(authorId);
        request.setAssigneeId(assigneeId);

        return request;
    }
}
