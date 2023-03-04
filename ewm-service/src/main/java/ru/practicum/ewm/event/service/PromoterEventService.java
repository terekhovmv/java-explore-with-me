package ru.practicum.ewm.event.service;

import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.EventShortDto;
import ru.practicum.ewm.api.dto.NewEventDto;
import ru.practicum.ewm.api.dto.UpdateEventUserRequest;

import java.util.List;

public interface PromoterEventService {
    EventFullDto add(long callerId, NewEventDto dto);

    EventFullDto update(long callerId, long id, UpdateEventUserRequest dto);

    EventFullDto get(long callerId, long id);

    List<EventShortDto> getMany(long callerId, int from, int size);
}
