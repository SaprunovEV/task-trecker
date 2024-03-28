package by.sapra.tasktrecker.task.web.v1.controller;

import by.sapra.tasktrecker.task.service.TaskService;
import by.sapra.tasktrecker.task.web.v1.mapper.TaskResponseMapper;
import by.sapra.tasktrecker.task.web.v1.model.TaskResponse;
import by.sapra.tasktrecker.task.web.v1.model.TaskUploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService service;
    private final TaskResponseMapper mapper;

    @GetMapping
    public ResponseEntity<Flux<TaskResponse>> handleFindAll() {
        return ResponseEntity.ok(mapper.fluxTaskModelToFluxTaskResponse(service.getAll()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> handleFindById(@PathVariable String id) {
        return mapper.monoTaskModelToMonoTaskResponse(service.getById(id))
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<TaskResponse>> handleCreationTask(@RequestBody TaskUploadRequest request) {
        return mapper.monoTaskModelToMonoTaskResponse(service.saveNewTask(mapper.monoRequestToModel(request)))
                .map(task -> ResponseEntity.status(CREATED).body(task));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> handleUpdateTask(@PathVariable String id, @RequestBody TaskUploadRequest request) {
        return mapper.monoTaskModelToMonoTaskResponse(service.updateTask(id, mapper.monoRequestToModel(request)))
                .map(ResponseEntity::ok);
    }
}
