package by.sapra.tasktrecker.testUtil;

import by.sapra.tasktrecker.user.web.v1.model.UserResponse;

import java.util.UUID;

public class UserResponseTestDataBuilder implements TestDataBuilder<UserResponse> {
    private String id = UUID.randomUUID().toString();
    private String name = "test_name";
    private String email = "test@email.test";

    private UserResponseTestDataBuilder() {
    }

    private UserResponseTestDataBuilder(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static UserResponseTestDataBuilder aUserResponse() {
        return new UserResponseTestDataBuilder();
    }

    public UserResponseTestDataBuilder withId(String id) {
        return id == this.id ? this : new UserResponseTestDataBuilder(id, name, email);
    }

    public UserResponseTestDataBuilder withName(String name) {
        return name == this.name ? this : new UserResponseTestDataBuilder(id, name, email);
    }

    public UserResponseTestDataBuilder withEmail(String email) {
        return email == this.email ? this : new UserResponseTestDataBuilder(id, name, email);
    }

    @Override
    public UserResponse build() {
        UserResponse result = new UserResponse();
        result.setId(id);
        result.setName(name);
        result.setEmail(email);
        return result;
    }
}
