package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.UsersApi;
import ru.practicum.ewm.api.dto.*;
import ru.practicum.ewm.api.dto.validation.NewEventDtoValidator;
import ru.practicum.ewm.api.dto.validation.RandomAccessPageRequestValidator;
import ru.practicum.ewm.api.dto.validation.UpdateEventUserRequestValidator;
import ru.practicum.ewm.event.service.PromoterEventService;
import ru.practicum.ewm.request.service.ParticipantRequestService;
import ru.practicum.ewm.request.service.PromoterRequestService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UsersApiController implements UsersApi {

    private final PromoterEventService promoterEventService;
    private final PromoterRequestService promoterRequestService;

    private final ParticipantRequestService participantRequestService;

    private final NewEventDtoValidator newEventDtoValidator;
    private final RandomAccessPageRequestValidator randomAccessPageRequestValidator;
    private final UpdateEventUserRequestValidator updateEventUserRequestValidator;

    @Override
    public ResponseEntity<EventFullDto> promoterAddEvent(Long userId, NewEventDto body) {
        newEventDtoValidator.requireValid(body);

        return new ResponseEntity<>(
                promoterEventService.add(userId, body),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<EventFullDto> promoterUpdateEvent(Long userId, Long eventId, UpdateEventUserRequest body) {
        updateEventUserRequestValidator.requireValid(body);

        return new ResponseEntity<>(
                promoterEventService.update(userId, eventId, body),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<EventFullDto> promoterGetEvent(Long userId, Long eventId) {
        return new ResponseEntity<>(
                promoterEventService.get(userId, eventId),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<EventShortDto>> promoterGetEvents(Long userId, Integer from, Integer size) {
        randomAccessPageRequestValidator.requireValid(from, size);

        return new ResponseEntity<>(
                promoterEventService.getMany(userId, from, size),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<ParticipationRequestDto> participantAddRequest(Long userId, Long eventId) {
        return new ResponseEntity<>(
                participantRequestService.add(userId, eventId),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<ParticipationRequestDto> participantCancelRequest(Long userId, Long requestId) {
        return new ResponseEntity<>(
                participantRequestService.cancel(userId, requestId),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<EventRequestStatusUpdateResult> promoterChangeRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest body) {
        return new ResponseEntity<>(
                promoterRequestService.changeStatuses(userId, eventId, body),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<ParticipationRequestDto>> promoterGetParticipants(Long userId, Long eventId) {
        return new ResponseEntity<>(
                promoterRequestService.getForEvent(userId, eventId),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<ParticipationRequestDto>> participantGetRequests(Long userId) {
        return new ResponseEntity<>(
                participantRequestService.getRequested(userId),
                HttpStatus.OK
        );
    }
}
