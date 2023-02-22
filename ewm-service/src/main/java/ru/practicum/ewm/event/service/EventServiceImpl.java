package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.NewEventDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.mapping.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    private final EventMapper mapper;

    @Override
    public EventFullDto add(long userId, NewEventDto dto) {
        User initiator = userRepository.require(userId);
        Category category = categoryRepository.require(dto.getCategory());

        Event created = eventRepository.save(
                mapper.transientFromDto(dto, initiator, category)
        );
        log.info("Event '{}' was successfully added with id {}", created.getTitle(), created.getId());
        return mapper.toDto(created, 0);
    }
}
