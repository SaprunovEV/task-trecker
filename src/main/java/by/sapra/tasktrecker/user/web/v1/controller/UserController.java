package by.sapra.tasktrecker.user.web.v1.controller;

import by.sapra.tasktrecker.user.service.UserService;
import by.sapra.tasktrecker.user.web.v1.mappers.ResponseMapper;
import by.sapra.tasktrecker.user.web.v1.model.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
}
