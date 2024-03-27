package by.sapra.tasktrecker.user.web.v1.controller;

import by.sapra.tasktrecker.user.service.UserService;
import by.sapra.tasktrecker.user.web.v1.mappers.ResponseMapper;
import by.sapra.tasktrecker.user.web.v1.model.UpsertUserRequest;
import by.sapra.tasktrecker.user.web.v1.model.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final ResponseMapper mapper;
    private final UserService service;

    @GetMapping
    public ResponseEntity<Flux<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(mapper.userModelListToUserResponseList(service.getAll()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> getById(@PathVariable String id) {
        return mapper.monoModelToMonoResponse(service.getById(id)).map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> createNewUser(@RequestBody UpsertUserRequest request) {
        return mapper.monoModelToMonoResponse(service.createNewUser(mapper.requestToModel(request)))
                .map(user -> ResponseEntity.status(CREATED).body(user));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> updateUser(@PathVariable String id, @RequestBody UpsertUserRequest request) {
        return mapper
                .monoModelToMonoResponse(service.updateUser(id, mapper.requestToModel(request)))
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return service.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }
}
