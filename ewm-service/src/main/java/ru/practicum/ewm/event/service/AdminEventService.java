package ru.practicum.ewm.event.service;

import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {
    EventFullDto update(long id, UpdateEventAdminRequest dto);

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
