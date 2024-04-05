package by.sapra.tasktrecker.task.model.storade;

import by.sapra.tasktrecker.task.model.TaskModel;
import by.sapra.tasktrecker.task.model.UserLinks;

public interface TaskUserStorage {
    UserLinks getUserLinks(TaskModel task);
}
