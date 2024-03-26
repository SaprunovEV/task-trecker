package by.sapra.tasktrecker.config;

import by.sapra.tasktrecker.testUtil.TestDbFacade;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ContextConfiguration(classes = AbstractDataConf.class)
@Testcontainers
public class AbstractDataTest {
    @Autowired
    protected TestDbFacade testDbFacade;

    @Container
    public static MongoDBContainer container = new MongoDBContainer("mongo:6.0.8")
            .withReuse(true);

    @DynamicPropertySource
    public static void handleProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
    }

    @AfterEach
    public void tearDown() {
        testDbFacade.deleteAll();
    }
}
