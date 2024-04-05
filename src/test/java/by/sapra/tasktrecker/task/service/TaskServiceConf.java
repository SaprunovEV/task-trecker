package by.sapra.tasktrecker.task.service;

import by.sapra.tasktrecker.task.model.repo.TaskRepository;
import by.sapra.tasktrecker.task.model.storade.TaskUserStorage;
import by.sapra.tasktrecker.task.service.impl.MongoTaskService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TaskServiceConf {
    @Bean
    public TaskService service(TaskRepository repo, TaskUserStorage storage) {
        return new MongoTaskService(repo, storage);
    }
}
