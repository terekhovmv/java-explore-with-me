package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.EventsApi;
import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.EventShortDto;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EventsApiController implements EventsApi {

    @Override
    public ResponseEntity<EventFullDto> getEvent1(Long id) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<List<EventShortDto>> getEvents1(
            String text,
            List<Long> categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Integer from,
            Integer size
    ) {
        //TODO
        throw new UnsupportedOperationException();
    }
}
