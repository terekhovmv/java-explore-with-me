package ru.practicum.ewm.event.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.UpdateEventAdminDto;
import ru.practicum.ewm.api.dto.mapping.DateTimeMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.mapping.EventMapper;
import ru.practicum.ewm.event.mapping.EventStateMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.EventAdminService;
import ru.practicum.ewm.exception.ForbiddenException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventAdminServiceImpl implements EventAdminService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    private final EventMapper eventMapper;
    private final DateTimeMapper dateTimeMapper;
    private final EventStateMapper eventStateMapper;

    @Override
    public EventFullDto update(long id, UpdateEventAdminDto dto) {
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
            toUpdate.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.isRequestModeration() != null) {
            toUpdate.setRequestModeration(dto.isRequestModeration());
        }
        if (dto.getStateAction() != null) {
            if (toUpdate.getState() != EventState.PENDING) {
                throw new ForbiddenException("The event is not in PENDING state");
            }

            if (dto.getStateAction() == UpdateEventAdminDto.StateActionEnum.PUBLISH_EVENT) {
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
    public List<EventFullDto> find(List<Long> filterUsers, List<EventFullDto.StateEnum> filterStates, List<Long> filterCategories, LocalDateTime filterStart, LocalDateTime filterEnd, int from, int size) {
        List<Event> found = eventRepository.find(
                filterUsers,
                (filterStates == null)
                        ? null
                        : filterStates.stream().map(eventStateMapper::toState).collect(Collectors.toList()),
                filterCategories,
                filterStart,
                filterEnd,
                from,
                size
        );

        return found
                .stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }
}
