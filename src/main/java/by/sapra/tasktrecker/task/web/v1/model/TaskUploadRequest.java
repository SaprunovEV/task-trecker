package by.sapra.tasktrecker.task.web.v1.model;

import by.sapra.tasktrecker.task.web.v1.model.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUploadRequest {
    private String name;
    private String description;
    private TaskStatus taskStatus;
    private String authorId;
    private String assigneeId;
}
