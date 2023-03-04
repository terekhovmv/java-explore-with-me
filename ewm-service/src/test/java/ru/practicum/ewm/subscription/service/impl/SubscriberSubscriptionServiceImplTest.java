package ru.practicum.ewm.subscription.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.subscription.impl.SubscriberSubscriptionServiceImpl;
import ru.practicum.ewm.subscription.model.Subscription;
import ru.practicum.ewm.subscription.repository.SubscriptionRepository;
import ru.practicum.ewm.user.mapping.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SubscriberSubscriptionServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriberSubscriptionServiceImpl testee;

    @Autowired
    private UserMapper userMapper;

    private long lastUserIdx = 0;

    @BeforeEach
    void beforeEach() {
        lastUserIdx = 0;
    }

    @Test
    void addAbsent() {
        User subscriber = createUser();
        User subscribedTo = createUser();

        when(
                userRepository.require(subscriber.getId())
        ).thenReturn(subscriber);
        when(
                userRepository.require(subscribedTo.getId())
        ).thenReturn(subscribedTo);

        when(
                subscriptionRepository.findFirstBySubscriberIdAndPromoterId(subscriber.getId(), subscribedTo.getId())
        ).thenReturn(Optional.empty());

        when(
                subscriptionRepository.save(any())
        ).thenAnswer(invocationOnMock -> {
            Subscription archetype = invocationOnMock.getArgument(0, Subscription.class);
            assertNull(archetype.getId());
            assertEquals(subscriber, archetype.getSubscriber());
            assertEquals(subscribedTo, archetype.getPromoter());
            assertNull(archetype.getSubscribedOn());

            return new Subscription(
                    1000L,
                    subscriber,
                    subscribedTo,
                    LocalDateTime.now()
            );
        });

        assertTrue(testee.add(subscriber.getId(), subscribedTo.getId()));
    }

    @Test
    void addExistent() {
        User subscriber = createUser();
        User subscribedTo = createUser();

        when(
                userRepository.require(subscriber.getId())
        ).thenReturn(subscriber);
        when(
                userRepository.require(subscribedTo.getId())
        ).thenReturn(subscribedTo);

        when(
                subscriptionRepository.findFirstBySubscriberIdAndPromoterId(subscriber.getId(), subscribedTo.getId())
        ).thenReturn(Optional.of(new Subscription()));

        assertFalse(testee.add(subscriber.getId(), subscribedTo.getId()));
        verify(subscriptionRepository, times(0)).save(any());
    }

    @Test
    void remove() {
        User subscriber = createUser();
        User subscriberTo = createUser();
        Subscription subscription = new Subscription(
                1000L,
                subscriber,
                subscriberTo,
                LocalDateTime.now()
        );

        when(
                userRepository.require(subscriber.getId())
        ).thenReturn(subscriber);
        when(
                userRepository.require(subscriberTo.getId())
        ).thenReturn(subscriberTo);

        when(
                subscriptionRepository.require(subscriber.getId(), subscriberTo.getId())
        ).thenReturn(subscription);

        testee.remove(subscriber.getId(), subscriberTo.getId());
        verify(subscriptionRepository, times(1)).delete(subscription);
    }

    @Test
    void getSubscribed() {
        final int from = 20;
        final int size = 10;
        User subscriber = createUser();
        List<User> subscribedTo = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            subscribedTo.add(createUser());
        }

        when(
                userRepository.require(subscriber.getId())
        ).thenReturn(subscriber);


        when(
                subscriptionRepository.getSubscribed(anyLong(), any())
        ).thenAnswer(invocationOnMock -> {
            Long subscriberId = invocationOnMock.getArgument(0, Long.class);
            Pageable pageable = invocationOnMock.getArgument(1, Pageable.class);

            assertEquals(subscriber.getId(), subscriberId);
            assertEquals(from, pageable.getOffset());
            assertEquals(size, pageable.getPageSize());

            return new PageImpl<>(subscribedTo);
        });

        assertIterableEquals(
                subscribedTo.stream().map(userMapper::toShortDto).collect(Collectors.toList()),
                testee.getMany(subscriber.getId(), from, size)
        );
    }

    private User createUser() {
        long idx = lastUserIdx++;
        String name = "user-name-" + idx;
        return new User(
                idx,
                name,
                name + "@abc.def"
        );
    }
}
