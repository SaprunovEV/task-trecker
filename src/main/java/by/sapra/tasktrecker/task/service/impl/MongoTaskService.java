package by.sapra.tasktrecker.task.service.impl;

import by.sapra.tasktrecker.task.model.TaskModel;
import by.sapra.tasktrecker.task.model.UserLinks;
import by.sapra.tasktrecker.task.model.repo.TaskRepository;
import by.sapra.tasktrecker.task.model.storade.TaskUserStorage;
import by.sapra.tasktrecker.task.service.TaskService;
import by.sapra.tasktrecker.user.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MongoTaskService implements TaskService {
    private final TaskRepository repository;
    private final TaskUserStorage storage;
    @Override
    public Flux<TaskModel> getAll() {
        return null;
    }

    @Override
    public Mono<TaskModel> getById(String id) {
        return repository.findById(id).flatMap(task -> {
            UserLinks userLinks = storage.getUserLinks(task);
            return zipTaskWithLinks(task, userLinks);
        });
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

    private static Mono<TaskModel> zipTaskWithLinks(TaskModel task, UserLinks userLinks) {
        return Mono.zip(
                Mono.just(task),
                userLinks.getAuthor(),
                userLinks.getAssignee(),
                Mono.zip(userLinks.getObservers(), (items) -> Arrays.stream(items).map(item -> (UserModel) item).collect(Collectors.toSet()))
        ).map(tuple4 -> {
            TaskModel result = tuple4.getT1();
            result.setAuthor(tuple4.getT2());
            result.setAssignee(tuple4.getT3());
            result.setObservers(tuple4.getT4());

            return result;
        });
    }
}
