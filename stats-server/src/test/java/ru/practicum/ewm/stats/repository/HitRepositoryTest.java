package ru.practicum.ewm.stats.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.ewm.stats.model.App;
import ru.practicum.ewm.stats.model.Hit;
import ru.practicum.ewm.stats.model.Uri;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class HitRepositoryTest {
    @Autowired
    private AppRepository appRepository;

    @Autowired
    private UriRepository uriRepository;

    @Autowired
    private HitRepository testee;

    @Test
    void addsHit() {
        App app = appRepository.save(new App(null, "app-name"));
        Uri uri = uriRepository.save(new Uri(null, "uri-path"));
        Hit archetype = new Hit(null, app, uri, "127.0.0.1", LocalDateTime.now());

        Hit created = testee.save(archetype);

        assertEquals(archetype.getApp(), created.getApp());
        assertEquals(archetype.getUri(), created.getUri());
        assertEquals(archetype.getIp(), created.getIp());
        assertEquals(archetype.getTimestamp(), created.getTimestamp());
        assertEquals(
                created,
                testee.getById(created.getId())
        );
    }
}