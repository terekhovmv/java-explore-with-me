package ru.practicum.ewm.event.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.EventShortDto;
import ru.practicum.ewm.api.dto.NewEventDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.mapping.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.EventPrivateService;
import ru.practicum.ewm.pagination.RandomAccessPageRequest;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventPrivateServiceImpl implements EventPrivateService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    @Override
    public EventFullDto add(long initiatorId, NewEventDto dto) {
        User initiator = userRepository.require(initiatorId);
        Category category = categoryRepository.require(dto.getCategory());

        Event created = eventRepository.save(
                eventMapper.transientFromDto(dto, initiator, category)
        );
        log.info("Event '{}' was successfully added with id {}", created.getTitle(), created.getId());
        return eventMapper.toDto(created);
    }

    @Override
    public EventFullDto get(long initiatorId, long id) {
        Event found = eventRepository.requireInitiated(id, initiatorId);

        return eventMapper.toDto(found);
    }

    @Override
    public List<EventShortDto> getMany(long initiatorId, int from, int size) {
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

        return found
                .stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }
}
