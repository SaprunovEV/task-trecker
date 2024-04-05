package by.sapra.tasktrecker.task.model.repo;

import by.sapra.tasktrecker.task.model.TaskModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends ReactiveMongoRepository<TaskModel, String> {
}
