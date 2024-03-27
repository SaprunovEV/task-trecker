package by.sapra.tasktrecker.config;

import by.sapra.tasktrecker.testUtil.TestDbFacade;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@TestConfiguration
@EnableReactiveMongoRepositories(basePackages = "by.sapra.tasktrecker")
public class AbstractDataConf {
    @Bean
    public TestDbFacade testDbFacade() {
        return new TestDbFacade();
    }
}
