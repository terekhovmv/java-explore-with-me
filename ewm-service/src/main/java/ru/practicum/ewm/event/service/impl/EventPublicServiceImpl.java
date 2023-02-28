package ru.practicum.ewm.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.EventShortDto;
import ru.practicum.ewm.event.mapping.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventSort;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.EventPublicService;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.stats.client.StatsProvider;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventPublicServiceImpl implements EventPublicService {

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final StatsProvider statsProvider;

    @Override
    public EventFullDto get(long id) {
        Event found = eventRepository
                .findPublishedById(id)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Event with id=%d was not found", id))
                );

        EventStats stats = new EventStats(statsProvider, found);
        eventRepository.save(stats.updateCachedViews(found));

        return eventMapper.toDto(found);
    }

    @Override
    public List<EventShortDto> find(
            String filterText,
            List<Long> filterCategories,
            Boolean filterPaid,
            LocalDateTime filterStart,
            LocalDateTime filterEnd,
            boolean filterOnlyAvailable,
            EventSort sort,
            Integer from,
            Integer size
    ) {
        //TODO
        throw new UnsupportedOperationException();
    }
}
