package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.EventsApi;
import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.EventShortDto;
import ru.practicum.ewm.api.dto.validation.RandomAccessPageRequestValidator;
import ru.practicum.ewm.api.dto.validation.StringDateTimeValidator;
import ru.practicum.ewm.api.dto.validation.StringEventSortValidator;
import ru.practicum.ewm.event.model.EventSort;
import ru.practicum.ewm.event.service.PublicEventService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class EventsApiController implements EventsApi {

    private final PublicEventService service;

    private final StringDateTimeValidator stringDateTimeValidator;
    private final StringEventSortValidator stringEventSortValidator;
    private final RandomAccessPageRequestValidator randomAccessPageRequestValidator;

    @Override
    public ResponseEntity<EventFullDto> getEvent(Long id) {
        return new ResponseEntity<>(
                service.get(id),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<EventShortDto>> findEvents(
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
        LocalDateTime filterStart = stringDateTimeValidator.requireValidOrNull(rangeStart, "rangeStart");
        LocalDateTime filterEnd = stringDateTimeValidator.requireValidOrNull(rangeEnd, "rangeEnd");
        EventSort eventSort = stringEventSortValidator.requireValidOrNull(sort, "sort");
        randomAccessPageRequestValidator.requireValid(from, size);

        return new ResponseEntity<>(
                service.find(
                        text,
                        categories,
                        paid,
                        filterStart,
                        filterEnd,
                        onlyAvailable,
                        eventSort,
                        from,
                        size
                ),
                HttpStatus.OK
        );
    }
}
