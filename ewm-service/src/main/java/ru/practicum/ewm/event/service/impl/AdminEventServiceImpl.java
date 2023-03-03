package ru.practicum.ewm.event.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.Location;
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

import static ru.practicum.ewm.utls.ValueApplier.applyNotNull;

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

        applyNotNull(toUpdate::setTitle, dto.getTitle());
        applyNotNull(toUpdate::setAnnotation, dto.getAnnotation());
        applyNotNull(toUpdate::setDescription, dto.getDescription());
        applyNotNull(toUpdate::setPaid, dto.isPaid());
        applyNotNull(toUpdate::setRequestModeration, dto.isRequestModeration());

        applyNotNull(this::updateCategory, toUpdate, dto.getCategory());
        applyNotNull(this::updateEventDate, toUpdate, dto.getEventDate());
        applyNotNull(this::updateLocation, toUpdate, dto.getLocation());
        applyNotNull(this::updateParticipantLimit, toUpdate, dto.getParticipantLimit());

        applyNotNull(this::applyStateAction, toUpdate, dto.getStateAction());

        Event updated = eventRepository.save(toUpdate);
        log.info("Event #{} was successfully updated", id);

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

    private void updateCategory(Event toUpdate, Long categoryId) {
        Category category = categoryRepository.require(categoryId);
        toUpdate.setCategory(category);
    }

    private void updateEventDate(Event toUpdate, String stringEventDate) {
        LocalDateTime newEventDate = dateTimeMapper.stringToDateTime(stringEventDate);
        if (!newEventDate.isAfter(LocalDateTime.now())) {
            throw new ConflictException("New event date is not acceptable");
        }
        toUpdate.setEventDate(newEventDate);
    }

    private void updateLocation(Event toUpdate, Location location) {
        toUpdate.setLocationLat(location.getLat());
        toUpdate.setLocationLon(location.getLon());
    }

    private void updateParticipantLimit(Event toUpdate, Integer participantLimit) {
        toUpdate.setParticipantLimit(participantLimit.longValue());
    }

    private void applyStateAction(Event toUpdate, UpdateEventAdminRequest.StateActionEnum stateAction) {
        if (toUpdate.getState() != EventState.PENDING) {
            throw new ConflictException("The event is not in PENDING state");
        }

        if (stateAction == UpdateEventAdminRequest.StateActionEnum.REJECT_EVENT) {
            toUpdate.setState(EventState.CANCELED);
            return;
        }

        assert stateAction == UpdateEventAdminRequest.StateActionEnum.PUBLISH_EVENT;
        if (!toUpdate.getEventDate().minusHours(PUBLISHING_DEADLINE_HOURS)
                .isAfter(LocalDateTime.now())
        ) {
            throw new ConflictException("Too late to publish the event");
        }
        toUpdate.setState(EventState.PUBLISHED);
    }
}
