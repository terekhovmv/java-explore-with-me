package ru.practicum.ewm.event.service;

import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.UpdateEventAdminDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAdminService {
    EventFullDto update(long id, UpdateEventAdminDto dto);

    List<EventFullDto> find(
            List<Long> filterUsers,
            List<EventFullDto.StateEnum> filterStates,
            List<Long> filterCategories,
            LocalDateTime filterStart,
            LocalDateTime filterEnd,
            int from,
            int size
    );
}
