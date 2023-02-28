package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.UsersApi;
import ru.practicum.ewm.api.dto.*;
import ru.practicum.ewm.api.dto.validation.NewEventDtoValidator;
import ru.practicum.ewm.api.dto.validation.RandomAccessPageRequestValidator;
import ru.practicum.ewm.event.service.EventPrivateService;
import ru.practicum.ewm.request.service.RequestPrivateService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UsersApiController implements UsersApi {

    private final EventPrivateService eventPrivateService;

    private final RequestPrivateService requestPrivateService;

    private final NewEventDtoValidator newEventDtoValidator;

    private final RandomAccessPageRequestValidator randomAccessPageRequestValidator;

    @Override
    public ResponseEntity<EventFullDto> addEvent(Long userId, NewEventDto body) {
        newEventDtoValidator.requireValid(body);

        return new ResponseEntity<>(
                eventPrivateService.add(userId, body),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<EventFullDto> updateEvent(Long userId, Long eventId, UpdateEventUserRequest body) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<EventFullDto> getInitiatedEvent(Long userId, Long eventId) {
        return new ResponseEntity<>(
                eventPrivateService.get(userId, eventId),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<EventShortDto>> getInitiatedEvents(Long userId, Integer from, Integer size) {
        randomAccessPageRequestValidator.requireValid(from, size);

        return new ResponseEntity<>(
                eventPrivateService.getMany(userId, from, size),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<ParticipationRequestDto> addParticipationRequest(Long userId, Long eventId) {
        return new ResponseEntity<>(
                requestPrivateService.add(userId, eventId),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<ParticipationRequestDto> cancelRequest(Long userId, Long requestId) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<EventRequestStatusUpdateResult> changeRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest body) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<List<ParticipationRequestDto>> getEventParticipants(Long userId, Long eventId) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<List<ParticipationRequestDto>> getUserRequests(Long userId) {
        return new ResponseEntity<>(
                requestPrivateService.getMany(userId),
                HttpStatus.OK
        );
    }
}
