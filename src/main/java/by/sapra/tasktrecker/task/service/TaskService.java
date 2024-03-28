package by.sapra.tasktrecker.task.service;

import by.sapra.tasktrecker.task.model.TaskModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {
    Flux<TaskModel> getAll();

    Mono<TaskModel> getById(String id);

    Mono<TaskModel> saveNewTask(Mono<TaskModel> model);
}
