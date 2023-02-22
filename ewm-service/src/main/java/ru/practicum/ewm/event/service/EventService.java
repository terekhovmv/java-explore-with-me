package ru.practicum.ewm.event.service;

import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.NewEventDto;
import ru.practicum.ewm.api.dto.UpdateEventAdminDto;

public interface EventService {
    EventFullDto add(long userId, NewEventDto dto);

    EventFullDto adminUpdate(long id, UpdateEventAdminDto dto);
}
