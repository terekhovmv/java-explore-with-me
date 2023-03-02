package ru.practicum.ewm.event.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class EventFilter {
    String text;
    List<Long> initiators;
    List<EventState> states;
    List<Long> categories;
    LocalDateTime start;
    LocalDateTime end;
    Boolean paid;
    boolean onlyAvailable;
}
