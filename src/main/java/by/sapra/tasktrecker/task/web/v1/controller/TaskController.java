package by.sapra.tasktrecker.task.web.v1.controller;

import by.sapra.tasktrecker.task.service.TaskService;
import by.sapra.tasktrecker.task.web.v1.mapper.TaskResponseMapper;
import by.sapra.tasktrecker.task.web.v1.model.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
}
