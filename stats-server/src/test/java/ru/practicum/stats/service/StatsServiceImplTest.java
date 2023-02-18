package ru.practicum.stats.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.RegisterHitDto;
import ru.practicum.stats.dto.SummaryDto;
import ru.practicum.stats.model.App;
import ru.practicum.stats.model.Hit;
import ru.practicum.stats.model.Summary;
import ru.practicum.stats.model.Uri;
import ru.practicum.stats.repository.AppRepository;
import ru.practicum.stats.repository.HitRepository;
import ru.practicum.stats.repository.UriRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class StatsServiceImplTest {
    private final LocalDateTime zeroTimestamp = LocalDateTime.of(2023, Month.FEBRUARY, 1, 9, 0, 0);
    @MockBean
    private AppRepository appRepository;
    @MockBean
    private UriRepository uriRepository;
    @MockBean
    private HitRepository hitRepository;
    @Autowired
    private StatsServiceImpl testee;

    @Test
    void registerHit() {
        final String appName = "app-name";
        final String uriPath = "uri-path";
        final String ip = "127.0.0.1";
        final LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        RegisterHitDto dto = new RegisterHitDto();
        dto.setApp(appName);
        dto.setUri(uriPath);
        dto.setIp(ip);
        dto.setTimestamp(timestamp);

        App app = new App((short) 123, appName);
        Uri uri = new Uri(456L, uriPath);

        when(
                appRepository.saveIfAbsentByName(appName)
        ).thenReturn(app);

        when(
                uriRepository.saveIfAbsentByPath(uriPath)
        ).thenReturn(uri);

        HitDto expected = new HitDto(789L, appName, uriPath, ip, timestamp);

        when(
                hitRepository.save(any())
        ).thenAnswer(invocationOnMock -> {
            Hit archetype = invocationOnMock.getArgument(0, Hit.class);
            assertEquals(app.getId(), archetype.getApp().getId());
            assertEquals(uri.getId(), archetype.getUri().getId());
            assertEquals(ip, archetype.getIp());
            assertEquals(timestamp, archetype.getTimestamp());

            return new Hit(
                    expected.getId(),
                    archetype.getApp(),
                    archetype.getUri(),
                    archetype.getIp(),
                    archetype.getTimestamp()
            );
        });

        assertEquals(expected, testee.registerHit(dto));
    }

    @Test
    void getEmptyStatsIfNoUrisRegistered() {
        final LocalDateTime start = getTimestamp(-100);
        final LocalDateTime end = getTimestamp(0);

        when(
                uriRepository.findAllByPathIn(any())
        ).thenReturn(List.of());

        assertTrue(testee.getStats(start, end, List.of(), true).isEmpty());
        assertTrue(testee.getStats(start, end, List.of(" ", "", "   "), true).isEmpty());
    }

    @Test
    void getEmptyStatsIfNoHitsRegistered() {
        final LocalDateTime start = getTimestamp(-100);
        final LocalDateTime end = getTimestamp(0);
        final boolean unique = true;

        List<Uri> uris = List.of(
                new Uri(1L, "/add"),
                new Uri(2L, "/bad"),
                new Uri(3L, "/cut")
        );
        List<Long> uriIds = uris
                .stream()
                .map(Uri::getId)
                .collect(Collectors.toList());
        List<String> uriPaths = uris
                .stream()
                .map(Uri::getPath)
                .collect(Collectors.toList());

        when(
                uriRepository.findAllByPathIn(uriPaths)
        ).thenReturn(uris);

        when(
                hitRepository.getSummaries(start, end, uriIds, unique)
        ).thenReturn(List.of());

        assertTrue(testee.getStats(start, end, uriPaths, unique).isEmpty());
    }

    @Test
    void getStats() {
        final LocalDateTime start = getTimestamp(-100);
        final LocalDateTime end = getTimestamp(0);
        final boolean unique = true;

        List<App> apps = List.of(
                new App((short) 1, "algol"),
                new App((short) 2, "basic"),
                new App((short) 3, "cobol")
        );
        List<Short> appIds = apps
                .stream()
                .map(App::getId)
                .collect(Collectors.toList());

        List<Uri> uris = List.of(
                new Uri(4L, "/add"),
                new Uri(5L, "/bad"),
                new Uri(6L, "/cut")
        );
        List<Long> uriIds = uris
                .stream()
                .map(Uri::getId)
                .collect(Collectors.toList());
        List<String> uriPaths = uris
                .stream()
                .map(Uri::getPath)
                .collect(Collectors.toList());

        List<Summary> summaries = List.of(
                new Summary((short) 1, 4L, 3),
                new Summary((short) 2, 5L, 2),
                new Summary((short) 3, 6L, 1)
        );

        when(
                uriRepository.findAllByPathIn(uriPaths)
        ).thenReturn(uris);

        when(
                appRepository.findAllByIdIn(appIds)
        ).thenReturn(apps);

        when(
                hitRepository.getSummaries(start, end, uriIds, unique)
        ).thenReturn(summaries);

        assertIterableEquals(
                List.of(
                        new SummaryDto("algol", "/add", 3),
                        new SummaryDto("basic", "/bad", 2),
                        new SummaryDto("cobol", "/cut", 1)
                ),
                testee.getStats(start, end, uriPaths, unique)
        );
    }

    private LocalDateTime getTimestamp(int timeOffset) {
        return zeroTimestamp.plusMinutes(timeOffset);
    }
}