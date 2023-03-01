package ru.practicum.ewm.event.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.EventShortDto;
import ru.practicum.ewm.api.dto.NewEventDto;
import ru.practicum.ewm.api.dto.UpdateEventPrivateDto;
import ru.practicum.ewm.api.dto.mapping.DateTimeMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.mapping.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.PromoterEventService;
import ru.practicum.ewm.pagination.RandomAccessPageRequest;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromoterEventServiceImpl implements PromoterEventService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    private final EventMapper eventMapper;
    private final DateTimeMapper dateTimeMapper;

    @Override
    public EventFullDto add(long callerId, NewEventDto dto) {
        User initiator = userRepository.require(callerId);
        Category category = categoryRepository.require(dto.getCategory());

        Event created = eventRepository.save(
                eventMapper.transientFromDto(dto, initiator, category)
        );
        log.info("Event '{}' was successfully added with id {}", created.getTitle(), created.getId());
        return eventMapper.toDto(created);
    }

    @Override
    public EventFullDto update(long callerId, long id, UpdateEventPrivateDto dto) {
        Event toUpdate = eventRepository.requireInitiated(id, callerId);

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
            toUpdate.setEventDate(dateTimeMapper.stringToDateTime(dto.getEventDate()));
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
            if (toUpdate.getState() == EventState.PENDING
                    && dto.getStateAction() == UpdateEventPrivateDto.StateActionEnum.CANCEL_REVIEW
            ) {
                toUpdate.setState(EventState.CANCELED);
            } else if (toUpdate.getState() == EventState.CANCELED
                    && dto.getStateAction() == UpdateEventPrivateDto.StateActionEnum.SEND_TO_REVIEW
            ) {
                toUpdate.setState(EventState.PENDING);
            }
        }

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
}
