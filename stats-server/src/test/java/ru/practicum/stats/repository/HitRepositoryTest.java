package ru.practicum.stats.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.stats.model.App;
import ru.practicum.stats.model.Hit;
import ru.practicum.stats.model.Summary;
import ru.practicum.stats.model.Uri;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@DataJpaTest
public class HitRepositoryTest {
    private final String annIp = "192.168.1.2";
    private final String bobIp = "192.168.1.3";
    private final String camIp = "192.168.1.4";
    private final LocalDateTime zeroTimestamp = LocalDateTime.of(2023, Month.FEBRUARY, 1, 9, 0, 0);
    @Autowired
    private AppRepository appRepository;
    @Autowired
    private UriRepository uriRepository;
    @Autowired
    private HitRepository testee;
    private App algolApp;
    private App basicApp;
    private App cobolApp;
    private Uri addUri;
    private Uri badUri;
    private Uri cutUri;

    @BeforeEach
    void beforeEach() {
        algolApp = addApp("algol");
        basicApp = addApp("basic");
        cobolApp = addApp("cobol");

        addUri = addUri("add");
        badUri = addUri("bad");
        cutUri = addUri("cut");
    }

    private App addApp(String name) {
        return appRepository.save(new App(null, name));
    }

    private Uri addUri(String path) {
        return uriRepository.save(new Uri(null, path));
    }

    private void addHit(App app, Uri uri, String ip, int timeOffset) {
        testee.save(
                new Hit(null, app, uri, ip, getTimestamp(timeOffset))
        );
    }

    private LocalDateTime getTimestamp(int timeOffset) {
        return zeroTimestamp.plusMinutes(timeOffset);
    }

    private Summary createSummary(App app, Uri uri, long hits) {
        return new Summary(app.getId(), uri.getId(), hits);
    }

    @Test
    void filtersByTimestamp() {
        addHit(algolApp, addUri, annIp, -101);
        addHit(basicApp, badUri, bobIp, -1);
        addHit(cobolApp, cutUri, camIp, 1);
        addHit(cobolApp, cutUri, camIp, 100);
        addHit(cobolApp, cutUri, camIp, 101);

        List<Summary> expected = List.of(
                createSummary(cobolApp, cutUri, 2),
                createSummary(basicApp, badUri, 1)
        );

        List<Summary> actual = testee.getSummaries(
                getTimestamp(-100),
                getTimestamp(100),
                List.of(addUri.getId(), badUri.getId(), cutUri.getId()),
                false
        );

        assertIterableEquals(expected, actual);
    }


    @Test
    void filtersByUriIds() {
        addHit(algolApp, addUri, annIp, -101);
        addHit(basicApp, badUri, bobIp, -1);
        addHit(cobolApp, cutUri, camIp, 1);
        addHit(cobolApp, cutUri, camIp, 100);
        addHit(cobolApp, cutUri, camIp, 101);

        List<Summary> expected = List.of(
                createSummary(cobolApp, cutUri, 2)
        );

        List<Summary> actual = testee.getSummaries(
                getTimestamp(-100),
                getTimestamp(100),
                List.of(cutUri.getId()),
                false
        );

        assertIterableEquals(expected, actual);
    }

    @Test
    void filtersDuplicatedIps() {
        addHit(algolApp, addUri, annIp, -101);
        addHit(basicApp, badUri, bobIp, -1);
        addHit(basicApp, badUri, camIp, -1);
        addHit(basicApp, badUri, camIp, -1);
        addHit(cobolApp, cutUri, camIp, 1);
        addHit(cobolApp, cutUri, camIp, 1);
        addHit(cobolApp, cutUri, camIp, 101);

        List<Summary> expected = List.of(
                createSummary(basicApp, badUri, 2),
                createSummary(cobolApp, cutUri, 1)
        );

        List<Summary> actual = testee.getSummaries(
                getTimestamp(-100),
                getTimestamp(100),
                List.of(badUri.getId(), cutUri.getId()),
                true
        );

        assertIterableEquals(expected, actual);
    }
}