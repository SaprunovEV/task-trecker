package by.sapra.tasktrecker.user.service;

import by.sapra.tasktrecker.user.model.repo.UserRepository;
import by.sapra.tasktrecker.user.service.impl.MongoUserService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UserServiceConf {
    @Bean
    public UserService service(UserRepository repo) {
        return new MongoUserService(repo);
    }
}
