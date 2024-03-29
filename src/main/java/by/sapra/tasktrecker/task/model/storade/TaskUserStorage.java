package by.sapra.tasktrecker.task.model.storade;

import by.sapra.tasktrecker.task.model.TaskModel;
import by.sapra.tasktrecker.task.model.UserLinks;
import org.springframework.stereotype.Component;

@Component
public interface TaskUserStorage {
    UserLinks getUserLinks(TaskModel task);
}
