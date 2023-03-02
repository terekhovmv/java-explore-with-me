package ru.practicum.ewm.event.repository;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventFilter;
import ru.practicum.ewm.event.model.EventSort;

import java.util.List;

public interface CustomEventRepository {
    List<Event> find(
            EventFilter filter,
            EventSort sort,
            int from,
            int size
    );
}
