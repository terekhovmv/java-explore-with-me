package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.EventsApi;
import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.EventShortDto;
import ru.practicum.ewm.event.service.EventService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EventsApiController implements EventsApi {

    private final EventService service;

    @Override
    public ResponseEntity<EventFullDto> getEventPublic(Long id) {
        return new ResponseEntity<>(
                service.getPublic(id),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<EventShortDto>> getEventsPublic(
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
