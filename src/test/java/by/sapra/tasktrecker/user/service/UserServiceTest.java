package by.sapra.tasktrecker.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserServiceConf.class)
class UserServiceTest {
    @Autowired
    UserService service;

    @Test
    void shouldDoSomething() throws Exception {

    }
}