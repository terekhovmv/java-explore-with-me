package ru.practicum.ewm.event.service;

import ru.practicum.ewm.api.model.EventFullDto;
import ru.practicum.ewm.api.model.NewEventDto;

public interface EventService {
    EventFullDto add(long userId, NewEventDto dto);
}
