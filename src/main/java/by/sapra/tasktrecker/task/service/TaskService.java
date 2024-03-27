package by.sapra.tasktrecker.task.service;

import by.sapra.tasktrecker.task.model.TaskModel;
import reactor.core.publisher.Flux;

public interface TaskService {
    Flux<TaskModel> getAll();
}
