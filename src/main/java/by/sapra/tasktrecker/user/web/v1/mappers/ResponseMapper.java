package by.sapra.tasktrecker.user.web.v1.mappers;

import by.sapra.tasktrecker.user.service.model.UserModel;
import by.sapra.tasktrecker.user.web.v1.model.UserResponse;
import reactor.core.publisher.Flux;

public interface ResponseMapper {
    Flux<UserResponse> userModelListToUserResponsList(Flux<UserModel> models);
}
