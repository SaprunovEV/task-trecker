package by.sapra.tasktrecker.user.web.v1.controller;

import by.sapra.tasktrecker.user.service.UserService;
import by.sapra.tasktrecker.user.web.v1.mappers.ResponseMapper;
import by.sapra.tasktrecker.user.web.v1.model.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final ResponseMapper mapper;
    private final UserService service;

    @GetMapping
    public Flux<UserResponse> getAllUsers() {
        return mapper.userModelListToUserResponseList(service.getAll());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> getById(@PathVariable String id) {
        return mapper.monoModelToMonoResponse(service.getById(id)).map(ResponseEntity::ok);
    }
}
