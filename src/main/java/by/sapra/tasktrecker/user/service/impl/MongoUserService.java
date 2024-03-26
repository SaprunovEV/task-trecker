package by.sapra.tasktrecker.user.service.impl;

import by.sapra.tasktrecker.user.model.repo.UserRepository;
import by.sapra.tasktrecker.user.service.UserService;
import by.sapra.tasktrecker.user.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
@RequiredArgsConstructor
public class MongoUserService implements UserService {

    private final UserRepository repository;

    @Override
    public Flux<UserModel> getAll() {
        return repository.findAll();
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
