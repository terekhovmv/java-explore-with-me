package ru.practicum.ewm.event.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.*;
import ru.practicum.ewm.api.dto.mapping.DateTimeMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.mapping.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.PromoterEventService;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.pagination.RandomAccessPageRequest;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.utls.ValueApplier.applyNotNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromoterEventServiceImpl implements PromoterEventService {

    private static final int PROMOTING_DEADLINE_HOURS = 2;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final DateTimeMapper dateTimeMapper;

    @Override
    public EventFullDto add(long callerId, NewEventDto dto) {
        User initiator = userRepository.require(callerId);
        Category category = categoryRepository.require(dto.getCategory());

        LocalDateTime eventDate = dateTimeMapper.stringToDateTime(dto.getEventDate());
        if (!eventDate.minusHours(PROMOTING_DEADLINE_HOURS)
                .isAfter(LocalDateTime.now())
        ) {
            throw new ConflictException("Too late to promote the event");
        }

        Event created = eventRepository.save(
                eventMapper.transientFromDto(dto, initiator, category)
        );
        log.info("Event '{}' was successfully added with id {}", created.getTitle(), created.getId());
        return eventMapper.toDto(created);
    }

    @Override
    public EventFullDto update(long callerId, long id, UpdateEventUserRequest dto) {
        Event toUpdate = eventRepository.requireInitiated(id, callerId);

        if (toUpdate.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Unable to change already published event");
        }

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
        log.info("Event #'{}' was successfully updated", id);

        return eventMapper.toDto(updated);
    }

    @Override
    public EventFullDto get(long callerId, long id) {
        Event found = eventRepository.requireInitiated(id, callerId);

        return eventMapper.toDto(found);
    }

    @Override
    public List<EventShortDto> getMany(long callerId, int from, int size) {
        List<Event> found = eventRepository
                .findAllByInitiatorId(
                        callerId,
                        RandomAccessPageRequest.of(
                                from,
                                size,
                                Sort.by(Sort.Direction.ASC, "eventDate")
                        )
                )
                .getContent();

        return found
                .stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    private void updateCategory(Event toUpdate, Long categoryId) {
        Category category = categoryRepository.require(categoryId);
        toUpdate.setCategory(category);
    }

    private void updateEventDate(Event toUpdate, String stringEventDate) {
        LocalDateTime newEventDate = dateTimeMapper.stringToDateTime(stringEventDate);
        if (!newEventDate.minusHours(PROMOTING_DEADLINE_HOURS)
                .isAfter(LocalDateTime.now())
        ) {
            throw new ConflictException("Too late to update event date");
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

    private void applyStateAction(Event toUpdate, UpdateEventUserRequest.StateActionEnum stateAction) {
        final EventState state = toUpdate.getState();

        if (state == EventState.PENDING
                && stateAction == UpdateEventUserRequest.StateActionEnum.CANCEL_REVIEW
        ) {
            toUpdate.setState(EventState.CANCELED);
        } else if (state == EventState.CANCELED
                && stateAction == UpdateEventUserRequest.StateActionEnum.SEND_TO_REVIEW
        ) {
            toUpdate.setState(EventState.PENDING);
        }
    }
}
