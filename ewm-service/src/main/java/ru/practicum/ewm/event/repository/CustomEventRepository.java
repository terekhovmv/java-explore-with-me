package ru.practicum.ewm.event.repository;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventSort;
import ru.practicum.ewm.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomEventRepository {
    List<Event> find(
            List<Long> filterUsers,
            List<EventState> filterStates,
            List<Long> filterCategories,
            LocalDateTime filterStart,
            LocalDateTime filterEnd,
            int from,
            int size
    );

    List<Event> find(
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
