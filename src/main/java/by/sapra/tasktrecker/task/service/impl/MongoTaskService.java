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
import reactor.util.function.Tuple4;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MongoTaskService implements TaskService {
    private final TaskRepository repository;
    private final TaskUserStorage storage;
    @Override
    public Flux<TaskModel> getAll() {
        return repository.findAll().flatMap(task -> {
            UserLinks userLinks = storage.getUserLinks(task);
            Flux<List<UserModel>> zip = Flux.zip(userLinks.getObservers(), (items) -> Arrays.stream(items).map((item) -> (UserModel) item).collect(Collectors.toList()));
            return Flux.zip(

                    Flux.just(task),
                    userLinks.getAuthor().defaultIfEmpty(new UserModel()).flux(),
                    userLinks.getAssignee().defaultIfEmpty(new UserModel()).flux(),
                    !userLinks.getObservers().isEmpty() ?
                            zip :
                            Flux.just(new ArrayList<UserModel>())
            ).flatMap(tuple4 -> {
                TaskModel result = tuple4.getT1();
                result.setAuthor(tuple4.getT2().getId() == null ? null : tuple4.getT2());
                result.setAssignee(tuple4.getT3().getId() == null ? null : tuple4.getT3());
                result.setObservers(tuple4.getT4().stream().filter(user -> user.getId() != null).collect(Collectors.toSet()));
                return Flux.just(result);
            });
        });
    }

    private static TaskModel getTaskModel(Tuple4<TaskModel, UserModel, UserModel, Set<UserModel>> tuple4) {
        TaskModel result = tuple4.getT1();
        result.setAuthor(tuple4.getT2().getId() == null ? null : tuple4.getT2());
        result.setAssignee(tuple4.getT3().getId() == null ? null : tuple4.getT3());
        result.setObservers(tuple4.getT4().stream().filter(user -> user.getId() != null).collect(Collectors.toSet()));
        return result;
    }

    @Override
    public Mono<TaskModel> getById(String id) {
        return repository.findById(id).flatMap(task -> {
            UserLinks userLinks = storage.getUserLinks(task);
            return zipTaskWithLinks(task, userLinks);
        });
    }

    @Override
    public Mono<TaskModel> saveNewTask(TaskModel model) {
        model.setId(UUID.randomUUID().toString());
        model.setUpdateAt(Instant.now());
        model.setCreateAt(Instant.now());
        return repository.save(model);
    }

    @Override
    public Mono<TaskModel> updateTask(String taskId, TaskModel task2update) {
        return repository.findById(taskId).flatMap(task -> {
            if (task2update.getDescription() != null) task.setDescription(task2update.getDescription());

            if (task2update.getName() != null) task.setName(task2update.getName());

            if (task2update.getAssignee() != null) task.setAssigneeId(task2update.getAssigneeId());

            task.setUpdateAt(Instant.now());
            return repository.save(task).flatMap(task2save -> zipTaskWithLinks(task2save, storage.getUserLinks(task2save)));
        });
    }

    @Override
    public Mono<TaskModel> addObserver(String taskId, String observerId) {
        return repository.findById(taskId).flatMap(task -> {
            if (task.getObserverIds() == null) task.setObserverIds(new HashSet<>());
            task.getObserverIds().add(observerId);
            return zipTaskWithLinks(task, storage.getUserLinks(task));
        });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    private static Mono<TaskModel> zipTaskWithLinks(TaskModel task, UserLinks userLinks) {
        Mono<Tuple4<TaskModel, UserModel, UserModel, Set<UserModel>>> zip = Mono.zip(
                Mono.just(task),
                userLinks.getAuthor().defaultIfEmpty(new UserModel()),
                userLinks.getAssignee().defaultIfEmpty(new UserModel()),
                userLinks.getObservers().isEmpty() ?
                        Mono.just(new HashSet<>()) :
                        Mono.zip(userLinks.getObservers(), (items) -> Arrays.stream(items).map(item -> (UserModel) item).collect(Collectors.toSet()))
        );
        return zip.flatMap(tuple4 -> {
            TaskModel result = getTaskModel(tuple4);
            return Mono.just(result);
        });
    }
}
