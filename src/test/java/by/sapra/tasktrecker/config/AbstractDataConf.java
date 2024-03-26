package by.sapra.tasktrecker.config;

import by.sapra.tasktrecker.testUtil.TestDbFacade;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AbstractDataConf {
    @Bean
    public TestDbFacade testDbFacade() {
        return new TestDbFacade();
    }
}
