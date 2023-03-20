package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.SubscriptionsApi;
import ru.practicum.ewm.api.dto.SubscriptionInfoDto;
import ru.practicum.ewm.api.dto.validation.RandomAccessPageRequestValidator;
import ru.practicum.ewm.subscription.service.PublicSubscriptionService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SubscriptionsApiController implements SubscriptionsApi {

    private final PublicSubscriptionService service;

    private final RandomAccessPageRequestValidator randomAccessPageRequestValidator;

    @Override
    public ResponseEntity<SubscriptionInfoDto> getSubscriptionInfo(Long promoterId) {
        return new ResponseEntity<>(
                service.getInfo(promoterId),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<SubscriptionInfoDto>> getTopSubscriptionInfos(Integer from, Integer size) {
        randomAccessPageRequestValidator.requireValid(from, size);

        return new ResponseEntity<>(
                service.getTopInfos(from, size),
                HttpStatus.OK
        );
    }
}
