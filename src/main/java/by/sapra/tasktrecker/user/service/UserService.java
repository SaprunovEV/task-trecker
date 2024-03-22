package by.sapra.tasktrecker.user.service;

import by.sapra.tasktrecker.user.service.model.UserModel;
import reactor.core.publisher.Flux;

public interface UserService {
    Flux<UserModel> getAll();
}
