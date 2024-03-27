package by.sapra.tasktrecker.user.model.repo;

import by.sapra.tasktrecker.user.model.UserModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserModel, String> {

}
