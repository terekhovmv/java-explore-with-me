package ru.practicum.ewm.subscription.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.EventShortDto;
import ru.practicum.ewm.api.dto.UserShortDto;
import ru.practicum.ewm.event.model.EventSort;
import ru.practicum.ewm.pagination.RandomAccessPageRequest;
import ru.practicum.ewm.subscription.SubscriberSubscriptionService;
import ru.practicum.ewm.subscription.model.Subscription;
import ru.practicum.ewm.subscription.repository.SubscriptionRepository;
import ru.practicum.ewm.user.mapping.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriberSubscriptionServiceImpl implements SubscriberSubscriptionService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    private final UserMapper userMapper;

    @Override
    public boolean add(long subscriberId, long promoterId) {
        User subscriber = userRepository.require(subscriberId);
        User promoter = userRepository.require(promoterId);
        if (subscriptionRepository.findFirstBySubscriberIdAndPromoterId(subscriberId, promoterId).isPresent()) {
            return false;
        }

        Subscription created = subscriptionRepository.save(
                new Subscription(
                        null,
                        subscriber,
                        promoter,
                        null
                )
        );
        log.info(
                "User '{}' was successfully subscribed to promoter '{}', subscription id: {}",
                subscriber.getName(),
                promoter.getName(),
                created.getId()
        );
        return true;
    }

    @Override
    public void remove(long subscriberId, long promoterId) {
        userRepository.require(subscriberId);
        userRepository.require(promoterId);
        Subscription toDelete = subscriptionRepository.require(subscriberId, promoterId);

        subscriptionRepository.delete(toDelete);
        log.info(
                "User '{}' was successfully unsubscribed from promoter '{}'",
                toDelete.getSubscriber().getName(),
                toDelete.getPromoter().getName()
        );
    }

    @Override
    public List<UserShortDto> getMany(long subscriberId, int from, int size) {
        userRepository.require(subscriberId);

        List<User> found = subscriptionRepository
                .getSubscribed(
                        subscriberId,
                        RandomAccessPageRequest.of(
                                from,
                                size,
                                Sort.unsorted()
                        )
                )
                .getContent();

        return found
                .stream()
                .map(userMapper::toShortDto)
                .collect(Collectors.toList());
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
