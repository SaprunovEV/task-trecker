package by.sapra.tasktrecker.task.web.v1.mapper;

import by.sapra.tasktrecker.task.model.TaskModel;
import by.sapra.tasktrecker.task.web.v1.model.TaskResponse;
import by.sapra.tasktrecker.task.web.v1.model.TaskUploadRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskResponseMapper {
    Flux<TaskResponse> fluxTaskModelToFluxTaskResponse(Flux<TaskModel> fluxTaskModel);

    Mono<TaskResponse> monoTaskModelToMonoTaskResponse(Mono<TaskModel> model);

    Mono<TaskModel> monoRequestToModel(TaskUploadRequest request);
}
