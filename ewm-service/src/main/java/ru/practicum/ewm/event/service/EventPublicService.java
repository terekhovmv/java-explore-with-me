package ru.practicum.ewm.event.service;

import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.EventShortDto;
import ru.practicum.ewm.event.model.EventSort;

import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {
    EventFullDto get(long id);

    List<EventShortDto> find(
            String filterText,
            List<Long> filterCategories,
            Boolean filterPaid,
            LocalDateTime filterStart,
            LocalDateTime filterEnd,
            boolean filterOnlyAvailable,
            EventSort sort,
            int from,
            int size
    );
}

