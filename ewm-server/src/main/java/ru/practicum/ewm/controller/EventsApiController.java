package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.EventsApi;

@Controller
@RequiredArgsConstructor
public class EventsApiController implements EventsApi {
    /*
    TODO:
    ResponseEntity<EventFullDto> getEvent1(Long id);
    ResponseEntity<List<EventShortDto>> getEvents1(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size);
    */
}
