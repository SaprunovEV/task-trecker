package by.sapra.tasktrecker.testUtil;

import by.sapra.tasktrecker.user.service.model.UserModel;

import java.util.UUID;

public class UserTestDataBuilder implements TestDataBuilder<UserModel> {
    private String id = UUID.randomUUID().toString();
    private String name = "test_name";
    private String email = "test@mail.test";


    private UserTestDataBuilder() {}

    private UserTestDataBuilder(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static UserTestDataBuilder aUser() {
        return new UserTestDataBuilder();
    }

    public UserTestDataBuilder withName(String name) {
        return name == this.name ? this : new UserTestDataBuilder(name, email);
    }

    public UserTestDataBuilder withEmail(String email) {
        return email == this.email ? this : new UserTestDataBuilder(name, email);
    }

    @Override
    public UserModel build() {
        UserModel user = new UserModel();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
