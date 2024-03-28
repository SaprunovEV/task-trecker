package by.sapra.tasktrecker.task.service;

import by.sapra.tasktrecker.task.service.impl.MongoTaskService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TaskServiceConf {
    @Bean
    public TaskService service() {
        return new MongoTaskService();
    }
}
