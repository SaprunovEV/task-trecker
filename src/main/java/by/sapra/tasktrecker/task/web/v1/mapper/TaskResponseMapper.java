package by.sapra.tasktrecker.task.web.v1.mapper;

import by.sapra.tasktrecker.task.model.TaskModel;
import by.sapra.tasktrecker.task.web.v1.model.TaskResponse;
import by.sapra.tasktrecker.task.web.v1.model.TaskUploadRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskResponseMapper {
    default Flux<TaskResponse> fluxTaskModelToFluxTaskResponse(Flux<TaskModel> fluxTaskModel) {
        return fluxTaskModel.map(this::taskModelToTaskResponse);
    }

    default Mono<TaskResponse> monoTaskModelToMonoTaskResponse(Mono<TaskModel> model) {
        return model.map(this::taskModelToTaskResponse);
    }

    TaskResponse taskModelToTaskResponse(TaskModel taskModel);

    TaskModel monoRequestToModel(TaskUploadRequest request);
}
