package ru.practicum.ewm.subscription.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.SubscriptionInfoDto;
import ru.practicum.ewm.subscription.service.PublicSubscriptionService;

import java.util.List;

@Service
@RequiredArgsConstructor
class PublicSubscriptionServiceImpl implements PublicSubscriptionService {

    @Override
    public SubscriptionInfoDto getInfo(long promoterId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SubscriptionInfoDto> getTopInfos(int from, int size) {
        throw new UnsupportedOperationException();
    }
}
