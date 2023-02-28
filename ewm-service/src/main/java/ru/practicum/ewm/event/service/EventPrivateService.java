package ru.practicum.ewm.event.service;

import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.EventShortDto;
import ru.practicum.ewm.api.dto.NewEventDto;

import java.util.List;

public interface EventPrivateService {
    EventFullDto add(long userId, NewEventDto dto);

    EventFullDto get(long initiatorId, long id);

    List<EventShortDto> getMany(long initiatorId, int from, int size);
}
