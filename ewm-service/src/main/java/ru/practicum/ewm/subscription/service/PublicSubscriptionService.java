package ru.practicum.ewm.subscription.service;

import ru.practicum.ewm.api.dto.SubscriptionInfoDto;

import java.util.List;

public interface PublicSubscriptionService {
    SubscriptionInfoDto getInfo(long promoterId);

    List<SubscriptionInfoDto> getTopInfos(int from, int size);
}
