package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.stats.client.StatsProvider;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventStats {
    private static final String GET_EVENT_ENDPOINT_URI = "/events/";

    private static final LocalDateTime MIN_DATE = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0);

    private final Map<String, Long> viewsByUri;

    public EventStats(StatsProvider statsProvider, Event event) {
        this(statsProvider, List.of(event));
    }

    public EventStats(StatsProvider statsProvider, List<Event> events) {
        List<String> uris = events
                .stream()
                .map(EventStats::getEventUri)
                .collect(Collectors.toList());

        viewsByUri = statsProvider.getCurrentAppStats(
                uris,
                MIN_DATE,
                LocalDateTime.now(),
                true
        );
    }

    private static String getEventUri(Event event) {
        return GET_EVENT_ENDPOINT_URI + event.getId();
    }

    public Event updateCachedViews(Event event) {
        Long views = viewsByUri.getOrDefault(getEventUri(event), null);
        if (views != null) {
            event.setCachedViews(views);
        }
        return event;
    }
}
