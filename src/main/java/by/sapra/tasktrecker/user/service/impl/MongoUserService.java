package by.sapra.tasktrecker.user.service.impl;

import by.sapra.tasktrecker.user.model.UserModel;
import by.sapra.tasktrecker.user.model.repo.UserRepository;
import by.sapra.tasktrecker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

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
        return repository.findById(id);
    }

    @Override
    public Mono<UserModel> createNewUser(UserModel model2create) {
        UserModel userToSave = new UserModel();

        userToSave.setName(model2create.getName());
        userToSave.setEmail(model2create.getEmail());
        userToSave.setId(UUID.randomUUID().toString());

        return repository.save(userToSave);
    }

    @Override
    public Mono<UserModel> updateUser(String id, UserModel model2update) {
        return repository.findById(id).flatMap(user -> {
            if (model2update.getName() != null && !model2update.getName().equals(user.getName()))
                user.setName(model2update.getName());
            if (model2update.getEmail() != null && !model2update.getEmail().equals(user.getEmail()))
                user.setEmail(model2update.getEmail());

            return repository.save(user);
        });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }
}
