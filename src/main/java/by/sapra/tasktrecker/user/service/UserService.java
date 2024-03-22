package by.sapra.tasktrecker.user.service;

import by.sapra.tasktrecker.user.service.model.UserModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Flux<UserModel> getAll();

    Mono<UserModel> getById(String id);
}
