package ru.practicum.ewm.event.service;

import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.NewEventDto;

public interface EventService {
    EventFullDto add(long userId, NewEventDto dto);
}
