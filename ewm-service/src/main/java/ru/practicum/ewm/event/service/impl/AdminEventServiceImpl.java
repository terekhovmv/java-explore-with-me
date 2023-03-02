package ru.practicum.ewm.event.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.api.dto.mapping.DateTimeMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.mapping.EventMapper;
import ru.practicum.ewm.event.mapping.EventStateMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventFilter;
import ru.practicum.ewm.event.model.EventSort;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.AdminEventService;
import ru.practicum.ewm.exception.ConflictException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private static final int PUBLISHING_DEADLINE_HOURS = 1;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final DateTimeMapper dateTimeMapper;
    private final EventStateMapper eventStateMapper;

    @Override
    public EventFullDto update(long id, UpdateEventAdminRequest dto) {
        Event toUpdate = eventRepository.require(id);

        if (dto.getCategory() != null) {
            Category category = categoryRepository.require(dto.getCategory());
            toUpdate.setCategory(category);
        }
        if (dto.getTitle() != null) {
            toUpdate.setTitle(dto.getTitle());
        }
        if (dto.getAnnotation() != null) {
            toUpdate.setAnnotation(dto.getAnnotation());
        }
        if (dto.getDescription() != null) {
            toUpdate.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            LocalDateTime newEventDate = dateTimeMapper.stringToDateTime(dto.getEventDate());
            if (!newEventDate.isAfter(LocalDateTime.now())) {
                throw new ConflictException("New event date is not acceptable");
            }
            toUpdate.setEventDate(newEventDate);
        }
        if (dto.getLocation() != null) {
            toUpdate.setLocationLat(dto.getLocation().getLat());
            toUpdate.setLocationLon(dto.getLocation().getLon());
        }
        if (dto.isPaid() != null) {
            toUpdate.setPaid(dto.isPaid());
        }
        if (dto.getParticipantLimit() != null) {
            toUpdate.setParticipantLimit((long) dto.getParticipantLimit());
        }
        if (dto.isRequestModeration() != null) {
            toUpdate.setRequestModeration(dto.isRequestModeration());
        }
        if (dto.getStateAction() != null) {
            if (toUpdate.getState() != EventState.PENDING) {
                throw new ConflictException("The event is not in PENDING state");
            }

            if (dto.getStateAction() == UpdateEventAdminRequest.StateActionEnum.PUBLISH_EVENT) {
                if (!toUpdate.getEventDate().minusHours(PUBLISHING_DEADLINE_HOURS)
                        .isAfter(LocalDateTime.now())
                ) {
                    throw new ConflictException("Too late to publish the event");
                }
                toUpdate.setState(EventState.PUBLISHED);
            } else {
                toUpdate.setState(EventState.CANCELED);
            }
        }

        Event updated = eventRepository.save(toUpdate);
        log.info("Event #'{}' was successfully updated", id);

        return eventMapper.toDto(updated);
    }

    @Override
    public List<EventFullDto> find(
            List<Long> filterUsers,
            List<EventFullDto.StateEnum> filterStates,
            List<Long> filterCategories,
            LocalDateTime filterStart,
            LocalDateTime filterEnd,
            int from,
            int size
    ) {
        List<Event> found = eventRepository.find(
                EventFilter.builder()
                        .initiators(filterUsers)
                        .states((filterStates == null)
                                ? null
                                : filterStates.stream().map(eventStateMapper::toState).collect(Collectors.toList())
                        )
                        .categories(filterCategories)
                        .start(filterStart)
                        .end(filterEnd)
                        .onlyAvailable(false)
                        .build(),
                EventSort.EVENT_DATE,
                from,
                size
        );

        return found
                .stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }
}
