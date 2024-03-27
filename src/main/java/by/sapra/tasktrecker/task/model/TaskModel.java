package by.sapra.tasktrecker.task.model;

import by.sapra.tasktrecker.user.model.UserModel;
import by.sapra.tasktrecker.user.web.v1.model.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskModel {
    private String id;
    private String name;
    private String description;
    private Instant createAt;
    private Instant updateAt;
    private String authorId;
    private String assigneeId;
    private Set<String> observerIds;
    private UserModel author;
    private UserModel assignee;
    private Set<UserModel> observers;
}
