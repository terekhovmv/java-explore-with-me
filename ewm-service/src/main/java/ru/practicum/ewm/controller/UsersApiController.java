package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.UsersApi;
import ru.practicum.ewm.api.model.*;
import ru.practicum.ewm.event.service.EventService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UsersApiController implements UsersApi {

    private final EventService eventService;

    @Override
    public ResponseEntity<EventFullDto> addEvent(Long userId, NewEventDto body) {
        //TODO validate body
        return new ResponseEntity<>(
                eventService.add(userId, body),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<EventFullDto> updateEvent(Long userId, Long eventId, UpdateEventUserRequest body) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<EventFullDto> getEvent(Long userId, Long eventId) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<List<EventShortDto>> getEvents(Long userId, Integer from, Integer size) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<ParticipationRequestDto> addParticipationRequest(Long userId, Long eventId) {
        //TODO
        throw new UnsupportedOperationException();
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
        //TODO
        throw new UnsupportedOperationException();
    }
}
