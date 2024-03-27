package by.sapra.tasktrecker.testUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class TestDbFacade {
    @Autowired
    private MongoTemplate mongoTemplate;

    public <T> T save(TestDataBuilder<T> builder) {
        return  mongoTemplate.save(builder.build());
    }

    public <T> T find(Object id, Class<T> entity) {
        return  mongoTemplate.findById(id, entity);
    }

    public void deleteAll() {
        mongoTemplate.dropCollection("usr");
    }
}
