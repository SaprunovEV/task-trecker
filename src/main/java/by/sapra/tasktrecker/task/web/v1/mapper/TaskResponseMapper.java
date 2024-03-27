package by.sapra.tasktrecker.task.web.v1.mapper;

import by.sapra.tasktrecker.task.model.TaskModel;
import by.sapra.tasktrecker.task.web.v1.model.TaskResponse;
import reactor.core.publisher.Flux;

public interface TaskResponseMapper {
    Flux<TaskResponse> fluxTaskModelToFluxTaskResponse(Flux<TaskModel> fluxTaskModel);
}
