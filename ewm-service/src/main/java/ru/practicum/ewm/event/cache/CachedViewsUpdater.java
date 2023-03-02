package ru.practicum.ewm.event.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.stats.client.StatsProvider;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CachedViewsUpdater {

    private static final int POP_LIMIT = 100;
    private final Set<Long> eventIdsToUpdate = new HashSet<>();

    private final StatsProvider statsProvider;
    private final EventRepository eventRepository;

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void updateCachedViews() {
        List<Long> eventIdsToUpdateNow = pop();
        if (eventIdsToUpdateNow.isEmpty()) {
            return;
        }

        ActualEventViews actual = new ActualEventViews(statsProvider, eventIdsToUpdateNow);
        Map<Long, Long> viewsByIds = new HashMap<>();
        for (long eventId : eventIdsToUpdateNow) {
            Long views = actual.getOrNull(eventId);
            if (views != null) {
                viewsByIds.put(eventId, views);
            }
        }

        eventRepository.updateCachedViews(viewsByIds);
    }

    public void scheduleCachedViewsUpdating(long eventId) {
        synchronized (eventIdsToUpdate) {
            eventIdsToUpdate.add(eventId);
        }
    }

    private List<Long> pop() {
        List<Long> result = new ArrayList<>();

        synchronized (eventIdsToUpdate) {
            Iterator<Long> iter = eventIdsToUpdate.iterator();
            while (iter.hasNext() && result.size() < POP_LIMIT) {
                long id = iter.next();
                result.add(id);
                iter.remove();
            }
        }

        return result;
    }
}
