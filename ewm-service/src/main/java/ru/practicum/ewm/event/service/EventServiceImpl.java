package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.EventShortDto;
import ru.practicum.ewm.api.dto.NewEventDto;
import ru.practicum.ewm.api.dto.UpdateEventAdminDto;
import ru.practicum.ewm.api.dto.mapping.DateTimeMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.mapping.EventMapper;
import ru.practicum.ewm.event.mapping.EventStateMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.pagination.RandomAccessPageRequest;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.stats.client.StatsProvider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private static final String GET_EVENT_ENDPOINT_URI = "/events/";
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final DateTimeMapper dateTimeMapper;
    private final EventStateMapper eventStateMapper;
    private final StatsProvider statsProvider;

    @Override
    public EventFullDto add(long userId, NewEventDto dto) {
        User initiator = userRepository.require(userId);
        Category category = categoryRepository.require(dto.getCategory());

        Event created = eventRepository.save(
                eventMapper.transientFromDto(dto, initiator, category)
        );
        log.info("Event '{}' was successfully added with id {}", created.getTitle(), created.getId());
        return eventMapper.toDto(created, null);
    }

    @Override
    public EventFullDto adminUpdate(long id, UpdateEventAdminDto dto) {
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

        EventStats stats = new EventStats(statsProvider, updated);
        return eventMapper.toDto(updated, stats);
    }

    @Override
    public List<EventFullDto> findByAdmin(
            List<Long> filterUsers,
            List<EventFullDto.StateEnum> filterStates,
            List<Long> filterCategories,
            LocalDateTime filterStart,
            LocalDateTime filterEnd,
            int from,
            int size
    ) {
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

        EventStats stats = new EventStats(statsProvider, found);

        return found
                .stream()
                .map(item -> eventMapper.toDto(item, stats))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> getInitiated(long initiatorId, int from, int size) {
        List<Event> found = eventRepository
                .findAllByInitiatorId(
                        initiatorId,
                        RandomAccessPageRequest.of(
                                from,
                                size,
                                Sort.by(Sort.Direction.ASC, "eventDate")
                        )
                )
                .getContent();

        EventStats stats = new EventStats(statsProvider, found);

        return found
                .stream()
                .map(item -> eventMapper.toShortDto(item, stats))
                .collect(Collectors.toList());
    }
}
