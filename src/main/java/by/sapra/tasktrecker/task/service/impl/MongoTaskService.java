package by.sapra.tasktrecker.task.service.impl;

import by.sapra.tasktrecker.task.model.TaskModel;
import by.sapra.tasktrecker.task.service.TaskService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MongoTaskService implements TaskService {
    @Override
    public Flux<TaskModel> getAll() {
        return null;
    }

    @Override
    public Mono<TaskModel> getById(String id) {
        return null;
    }

    @Override
    public Mono<TaskModel> saveNewTask(Mono<TaskModel> model) {
        return null;
    }

    @Override
    public Mono<TaskModel> updateTask(String taskId, Mono<TaskModel> task2update) {
        return null;
    }

    @Override
    public Mono<TaskModel> addObserver(String taskId, String observerId) {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return null;
    }
}
