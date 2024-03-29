package by.sapra.tasktrecker.task.model;

import by.sapra.tasktrecker.user.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tsk")
public class TaskModel {
    @Id
    private String id;
    private String name;
    private String description;
    @EqualsAndHashCode.Exclude
    private Instant createAt;
    @EqualsAndHashCode.Exclude
    private Instant updateAt;
    private String authorId;
    private String assigneeId;
    private Set<String> observerIds;
    @ReadOnlyProperty
    private UserModel author;
    @ReadOnlyProperty
    private UserModel assignee;
    @ReadOnlyProperty
    private Set<UserModel> observers;
}
