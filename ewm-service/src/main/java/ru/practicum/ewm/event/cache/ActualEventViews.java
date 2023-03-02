package ru.practicum.ewm.event.cache;

import ru.practicum.stats.client.StatsProvider;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ActualEventViews {
    private static final String GET_EVENT_ENDPOINT_URI = "/events/";

    private static final LocalDateTime MIN_DATE = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0);

    private final Map<String, Long> viewsByUri;

    public ActualEventViews(StatsProvider statsProvider, List<Long> ids) {
        List<String> uris = ids
                .stream()
                .map(ActualEventViews::getEventUri)
                .collect(Collectors.toList());

        viewsByUri = statsProvider.getCurrentAppStats(
                uris,
                MIN_DATE,
                LocalDateTime.now(),
                true
        );
    }

    private static String getEventUri(long eventId) {
        return GET_EVENT_ENDPOINT_URI + eventId;
    }

    public Long getOrNull(long eventId) {
        return viewsByUri.getOrDefault(getEventUri(eventId), null);
    }
}
