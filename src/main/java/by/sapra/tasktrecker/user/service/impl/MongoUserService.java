package by.sapra.tasktrecker.user.service.impl;

import by.sapra.tasktrecker.user.service.UserService;
import by.sapra.tasktrecker.user.service.model.UserModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class MongoUserService implements UserService {
    @Override
    public Flux<UserModel> getAll() {
        return null;
    }

    @Override
    public Mono<UserModel> getById(String id) {
        return null;
    }

    @Override
    public Mono<UserModel> createNewUser(UserModel model2create) {
        return null;
    }

    @Override
    public Mono<UserModel> updateUser(String id, UserModel model2update) {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return null;
    }
}
