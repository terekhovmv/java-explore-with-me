package ru.practicum.ewm.subscription;

import ru.practicum.ewm.api.dto.EventShortDto;
import ru.practicum.ewm.api.dto.UserShortDto;
import ru.practicum.ewm.event.model.EventSort;

import java.util.List;

public interface SubscriberSubscriptionService {

    boolean add(long subscriberId, long promoterId);

    void remove(long subscriberId, long promoterId);

    List<UserShortDto> getMany(long subscriberId, int from, int size);

    List<EventShortDto> getFeed(
            Long subscriberId,
            String filterText,
            List<Long> filterCategories,
            Boolean filterPaid,
            boolean filterOnlyAvailable,
            EventSort sort,
            int from,
            int size
    );
}
