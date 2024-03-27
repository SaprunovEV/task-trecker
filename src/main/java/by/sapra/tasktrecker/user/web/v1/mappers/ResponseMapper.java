package by.sapra.tasktrecker.user.web.v1.mappers;

import by.sapra.tasktrecker.user.model.UserModel;
import by.sapra.tasktrecker.user.web.v1.model.UpsertUserRequest;
import by.sapra.tasktrecker.user.web.v1.model.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResponseMapper {
    default Flux<UserResponse> userModelListToUserResponseList(Flux<UserModel> models){
        return models.map(this::userModelToUserResponse);
    }

    default Mono<UserResponse> monoModelToMonoResponse(Mono<UserModel> model) {
        return model.map(this::userModelToUserResponse);
    }

    UserResponse userModelToUserResponse(UserModel userModel);

    UserModel requestToModel(UpsertUserRequest request);
}
