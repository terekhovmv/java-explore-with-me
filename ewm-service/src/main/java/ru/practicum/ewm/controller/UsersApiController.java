package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.UsersApi;
import ru.practicum.ewm.api.dto.*;
import ru.practicum.ewm.api.dto.validation.NewEventDtoValidator;
import ru.practicum.ewm.api.dto.validation.RandomAccessPageRequestValidator;
import ru.practicum.ewm.api.dto.validation.StringEventSortValidator;
import ru.practicum.ewm.api.dto.validation.UpdateEventUserRequestValidator;
import ru.practicum.ewm.event.model.EventSort;
import ru.practicum.ewm.event.service.PromoterEventService;
import ru.practicum.ewm.request.service.ParticipantRequestService;
import ru.practicum.ewm.request.service.PromoterRequestService;
import ru.practicum.ewm.subscription.SubscriberSubscriptionService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UsersApiController implements UsersApi {

    private final PromoterEventService promoterEventService;
    private final PromoterRequestService promoterRequestService;

    private final ParticipantRequestService participantRequestService;

    private final SubscriberSubscriptionService subscriberSubscriptionService;

    private final NewEventDtoValidator newEventDtoValidator;
    private final RandomAccessPageRequestValidator randomAccessPageRequestValidator;
    private final UpdateEventUserRequestValidator updateEventUserRequestValidator;
    private final StringEventSortValidator stringEventSortValidator;

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

    @Override
    public ResponseEntity<Void> subscriberAddSubscription(Long userId, Long promoterId) {
        boolean added = subscriberSubscriptionService.add(userId, promoterId);
        return new ResponseEntity<>(added ? HttpStatus.CREATED : HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> subscriberRemoveSubscription(Long userId, Long promoterId) {
        subscriberSubscriptionService.remove(userId, promoterId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<UserShortDto>> subscriberGetSubscriptions(
            Long userId,
            Integer from,
            Integer size
    ) {
        randomAccessPageRequestValidator.requireValid(from, size);

        return new ResponseEntity<>(
                subscriberSubscriptionService.getMany(userId, from, size),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<EventShortDto>> subscriberGetFeed(
            Long userId,
            String text,
            List<Long> categories,
            Boolean paid,
            Boolean onlyAvailable,
            String sort,
            Integer from,
            Integer size
    ) {
        EventSort eventSort = stringEventSortValidator.requireValidOrNull(sort, "sort");
        randomAccessPageRequestValidator.requireValid(from, size);

        return new ResponseEntity<>(
                subscriberSubscriptionService.getFeed(
                        userId,
                        text,
                        categories,
                        paid,
                        onlyAvailable,
                        eventSort,
                        from,
                        size
                ),
                HttpStatus.OK
        );
    }
}
