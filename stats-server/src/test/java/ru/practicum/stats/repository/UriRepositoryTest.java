package ru.practicum.stats.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.stats.model.Uri;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UriRepositoryTest {
    @Autowired
    private UriRepository testee;

    @Test
    void addsUri() {
        Uri archetype = new Uri(null, "uri-path");
        Uri created = testee.save(archetype);

        assertEquals(archetype.getPath(), created.getPath());
        assertEquals(
                created,
                testee.findById(created.getId()).get()
        );
    }
}