package by.sapra.tasktrecker.user.web.v1.mappers;

import by.sapra.tasktrecker.user.model.UserModel;
import by.sapra.tasktrecker.user.web.v1.model.UpsertUserRequest;
import by.sapra.tasktrecker.user.web.v1.model.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ResponseMapper {
    Flux<UserResponse> userModelListToUserResponseList(Flux<UserModel> models);

    Mono<UserResponse> monoModelToMonoResponse(Mono<UserModel> model);

    UserModel requestToModel(UpsertUserRequest request);
}
