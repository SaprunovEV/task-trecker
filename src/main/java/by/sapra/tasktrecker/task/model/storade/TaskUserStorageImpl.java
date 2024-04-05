package by.sapra.tasktrecker.task.model.storade;

import by.sapra.tasktrecker.task.model.TaskModel;
import by.sapra.tasktrecker.task.model.UserLinks;
import by.sapra.tasktrecker.user.model.UserModel;
import by.sapra.tasktrecker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskUserStorageImpl implements TaskUserStorage {
    private final UserService service;
    @Override
    public UserLinks getUserLinks(TaskModel task) {
        return UserLinks.builder()
                .author(service.getById(task.getAuthorId()))
                .assignee(service.getById(task.getAssigneeId()))
                .observers(task.getObserverIds().stream().map(service::getById).toList()
                        .stream().map(mono -> mono.defaultIfEmpty(new UserModel())).toList())
                .build();
    }
}
