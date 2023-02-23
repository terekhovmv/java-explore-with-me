package ru.practicum.ewm.event.service;

import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.EventShortDto;
import ru.practicum.ewm.api.dto.NewEventDto;
import ru.practicum.ewm.api.dto.UpdateEventAdminDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto add(long userId, NewEventDto dto);

    EventFullDto adminUpdate(long id, UpdateEventAdminDto dto);

    List<EventFullDto> findByAdmin(
            List<Long> filterUsers,
            List<EventFullDto.StateEnum> filterStates,
            List<Long> filterCategories,
            LocalDateTime filterStart,
            LocalDateTime filterEnd,
            int from,
            int size
    );

    List<EventShortDto> getInitiated(long initiatorId, int from, int size);
}
