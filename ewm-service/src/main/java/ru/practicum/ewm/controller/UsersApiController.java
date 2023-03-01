package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.UsersApi;
import ru.practicum.ewm.api.dto.*;
import ru.practicum.ewm.api.dto.validation.NewEventDtoValidator;
import ru.practicum.ewm.api.dto.validation.RandomAccessPageRequestValidator;
import ru.practicum.ewm.api.dto.validation.UpdateEventPrivateDtoValidator;
import ru.practicum.ewm.event.service.PromoterEventService;
import ru.practicum.ewm.request.service.PromoterRequestService;
import ru.practicum.ewm.request.service.RequesterRequestService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UsersApiController implements UsersApi {

    private final PromoterEventService promoterEventService;
    private final PromoterRequestService promoterRequestService;

    private final RequesterRequestService requesterRequestService;

    private final NewEventDtoValidator newEventDtoValidator;
    private final RandomAccessPageRequestValidator randomAccessPageRequestValidator;
    private final UpdateEventPrivateDtoValidator updateEventPrivateDtoValidator;

    @Override
    public ResponseEntity<EventFullDto> addEvent(Long userId, NewEventDto body) {
        newEventDtoValidator.requireValid(body);

        return new ResponseEntity<>(
                promoterEventService.add(userId, body),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<EventFullDto> updateEvent(Long userId, Long eventId, UpdateEventPrivateDto body) {
        updateEventPrivateDtoValidator.requireValid(body);

        return new ResponseEntity<>(
                promoterEventService.update(userId, eventId, body),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<EventFullDto> getInitiatedEvent(Long userId, Long eventId) {
        return new ResponseEntity<>(
                promoterEventService.get(userId, eventId),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<EventShortDto>> getInitiatedEvents(Long userId, Integer from, Integer size) {
        randomAccessPageRequestValidator.requireValid(from, size);

        return new ResponseEntity<>(
                promoterEventService.getMany(userId, from, size),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<ParticipationRequestDto> addParticipationRequest(Long userId, Long eventId) {
        return new ResponseEntity<>(
                requesterRequestService.add(userId, eventId),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<ParticipationRequestDto> cancelRequest(Long userId, Long requestId) {
        return new ResponseEntity<>(
                requesterRequestService.cancel(userId, requestId),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<EventRequestStatusUpdateResult> changeRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest body) {
        return new ResponseEntity<>(
                promoterRequestService.changeStatuses(userId, eventId, body),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<ParticipationRequestDto>> getEventParticipants(Long userId, Long eventId) {
        return new ResponseEntity<>(
                promoterRequestService.getForEvent(userId, eventId),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<ParticipationRequestDto>> getUserRequests(Long userId) {
        return new ResponseEntity<>(
                requesterRequestService.getRequested(userId),
                HttpStatus.OK
        );
    }
}
