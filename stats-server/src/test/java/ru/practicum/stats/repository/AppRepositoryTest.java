package ru.practicum.stats.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.stats.model.App;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class AppRepositoryTest {
    @Autowired
    private AppRepository testee;

    @Test
    void addsApp() {
        App archetype = new App(null, "app-name");
        App created = testee.save(archetype);

        assertEquals(archetype.getName(), created.getName());
        assertEquals(
                created,
                testee.findById(created.getId()).get()
        );
    }
}