package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.SubscribersApi;
import ru.practicum.ewm.api.dto.EventShortDto;
import ru.practicum.ewm.api.dto.UserShortDto;
import ru.practicum.ewm.api.dto.validation.RandomAccessPageRequestValidator;
import ru.practicum.ewm.api.dto.validation.StringEventSortValidator;
import ru.practicum.ewm.event.model.EventSort;
import ru.practicum.ewm.subscription.SubscriberSubscriptionService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SubscribersApiController implements SubscribersApi {

    private final SubscriberSubscriptionService service;

    private final StringEventSortValidator stringEventSortValidator;
    private final RandomAccessPageRequestValidator randomAccessPageRequestValidator;

    @Override
    public ResponseEntity<Void> subscriberAddSubscription(Long subscriberId, Long promoterId) {
        service.add(subscriberId, promoterId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> subscriberRemoveSubscription(Long subscriberId, Long promoterId) {
        service.remove(subscriberId, promoterId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<UserShortDto>> subscriberGetSubscriptions(
            Long subscriberId,
            Integer from,
            Integer size
    ) {
        randomAccessPageRequestValidator.requireValid(from, size);

        return new ResponseEntity<>(
                service.getMany(subscriberId, from, size),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<EventShortDto>> subscriberGetFeed(
            Long subscriberId,
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
                service.getFeed(
                        subscriberId,
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
