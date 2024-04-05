package by.sapra.tasktrecker.task.web.v1.model;

import by.sapra.tasktrecker.task.web.v1.model.enums.TaskStatus;
import by.sapra.tasktrecker.user.web.v1.model.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    private String id;
    private String name;
    private String description;
    private Instant createAt;
    private Instant updateAt;
    private TaskStatus taskStatus;
    private UserResponse author;
    private UserResponse assignee;
    private Set<UserResponse> observers;
}
