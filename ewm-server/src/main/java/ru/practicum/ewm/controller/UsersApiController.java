package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.UsersApi;

@Controller
@RequiredArgsConstructor
public class UsersApiController implements UsersApi {
    /*
    TODO:
    ResponseEntity<EventFullDto> addEvent(Long userId, NewEventDto body);
    ResponseEntity<ParticipationRequestDto> addParticipationRequest(Long userId, Long eventId);
    ResponseEntity<ParticipationRequestDto> cancelRequest(Long userId, Long requestId);
    ResponseEntity<EventRequestStatusUpdateResult> changeRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest body);
    ResponseEntity<EventFullDto> getEvent(Long userId, Long eventId);
    ResponseEntity<List<ParticipationRequestDto>> getEventParticipants(Long userId, Long eventId);
    ResponseEntity<List<EventShortDto>> getEvents(Long userId, Integer from, Integer size);
    ResponseEntity<List<ParticipationRequestDto>> getUserRequests(Long userId);
    ResponseEntity<EventFullDto> updateEvent(Long userId, Long eventId, UpdateEventUserRequest body);
    */
}
