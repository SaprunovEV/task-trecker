package by.sapra.tasktrecker.task.service;

import by.sapra.tasktrecker.config.AbstractDataTest;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ContextConfiguration(classes = TaskServiceConf.class)
class TaskServiceTest extends AbstractDataTest {

}