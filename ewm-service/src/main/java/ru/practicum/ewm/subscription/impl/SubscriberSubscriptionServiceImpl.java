package ru.practicum.ewm.subscription.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.EventShortDto;
import ru.practicum.ewm.api.dto.UserShortDto;
import ru.practicum.ewm.event.model.EventSort;
import ru.practicum.ewm.subscription.SubscriberSubscriptionService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriberSubscriptionServiceImpl implements SubscriberSubscriptionService {
    @Override
    public void add(long subscriberId, long promoterId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(long subscriberId, long promoterId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<UserShortDto> getMany(long subscriberId, int from, int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<EventShortDto> getFeed(
            Long subscriberId,
            String filterText,
            List<Long> filterCategories,
            Boolean filterPaid,
            boolean filterOnlyAvailable,
            EventSort sort,
            int from,
            int size
    ) {
        throw new UnsupportedOperationException();
    }
}
