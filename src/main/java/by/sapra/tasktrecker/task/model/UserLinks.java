package by.sapra.tasktrecker.task.model;

import by.sapra.tasktrecker.user.model.UserModel;
import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.List;

@Data
@Builder
public class UserLinks {
    private Mono<UserModel> author;
    private Mono<UserModel> assignee;
    private List<Mono<UserModel>> observers;
}
