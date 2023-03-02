package ru.practicum.ewm.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.EventShortDto;
import ru.practicum.ewm.event.cache.CachedViewsUpdater;
import ru.practicum.ewm.event.mapping.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventFilter;
import ru.practicum.ewm.event.model.EventSort;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.PublicEventService;
import ru.practicum.ewm.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final CachedViewsUpdater cachedViewsUpdater;

    @Override
    @Transactional
    public EventFullDto get(long id) {
        Event found = eventRepository
                .findPublishedById(id)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Event with id=%d was not found", id))
                );
        cachedViewsUpdater.scheduleCachedViewsUpdating(id);

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
            int from,
            int size
    ) {
        List<Event> found = eventRepository.find(
                EventFilter.builder()
                        .text(filterText)
                        .categories(filterCategories)
                        .paid(filterPaid)
                        .start(filterStart)
                        .end(filterEnd)
                        .onlyAvailable(filterOnlyAvailable)
                        .build(),
                sort,
                from,
                size
        );

        return found
                .stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }
}
