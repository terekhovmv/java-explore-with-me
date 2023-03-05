package ru.practicum.ewm.subscription.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.mapping.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventFilter;
import ru.practicum.ewm.event.model.EventSort;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
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
import static org.mockito.Mockito.*;

@SpringBootTest
public class SubscriberSubscriptionServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SubscriptionRepository subscriptionRepository;

    @MockBean
    private EventRepository eventRepository;

    @Autowired
    private SubscriberSubscriptionServiceImpl testee;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EventMapper eventMapper;

    private long lastUserIdx = 0;
    private long lastCategoryIdx = 0;
    private long lastEventIdx = 0;

    @BeforeEach
    void beforeEach() {
        lastUserIdx = 0;
        lastCategoryIdx = 0;
        lastEventIdx = 0;
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
    void addReflective() {
        User subscriber = createUser();

        when(
                userRepository.require(subscriber.getId())
        ).thenReturn(subscriber);

        assertThrows(ConflictException.class, () -> testee.add(subscriber.getId(), subscriber.getId()));
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


    @Test
    void getFeed() {
        final String filterText = "abc";
        final List<Long> filterCategories = List.of(1L, 3L, 5L);
        final Boolean filterPaid = false;
        final boolean filterOnlyAvailable = true;
        final EventSort sort = EventSort.VIEWS;
        final int from = 20;
        final int size = 10;
        User subscriber = createUser();
        List<Long> subscribedTo = List.of(
                createUser().getId(),
                createUser().getId(),
                createUser().getId()
        );
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            events.add(createEvent());
        }

        when(
                userRepository.require(subscriber.getId())
        ).thenReturn(subscriber);

        when(
                subscriptionRepository.getSubscribedIds(subscriber.getId())
        ).thenReturn(subscribedTo);

        when(
                eventRepository.find(any(), any(EventSort.class), anyInt(), anyInt())
        ).thenAnswer(invocationOnMock -> {
            EventFilter findFilter = invocationOnMock.getArgument(0, EventFilter.class);
            EventSort findSort = invocationOnMock.getArgument(1, EventSort.class);
            int findFrom = invocationOnMock.getArgument(2, Integer.class);
            int findSize = invocationOnMock.getArgument(3, Integer.class);

            assertEquals(
                    EventFilter.builder()
                            .states(List.of(EventState.PUBLISHED))
                            .text(filterText)
                            .categories(filterCategories)
                            .initiators(subscribedTo)
                            .paid(filterPaid)
                            .onlyAvailable(filterOnlyAvailable)
                            .build(),
                    findFilter
            );
            assertEquals(sort, findSort);
            assertEquals(from, findFrom);
            assertEquals(size, findSize);

            return events;
        });

        assertIterableEquals(
                events.stream().map(eventMapper::toShortDto).collect(Collectors.toList()),
                testee.getFeed(
                        subscriber.getId(),
                        filterText,
                        filterCategories,
                        filterPaid,
                        filterOnlyAvailable,
                        sort,
                        from,
                        size
                )
        );
    }

    private User createUser() {
        long idx = lastUserIdx++;
        String name = "user-" + idx;
        return new User(
                idx,
                name,
                name + "@abc.def"
        );
    }

    private Category createCategory() {
        long idx = lastCategoryIdx;
        return new Category(idx, "name-" + idx);
    }

    private Event createEvent() {
        long idx = lastEventIdx++;
        return Event.builder()
                .id(idx)
                .initiator(createUser())
                .category(createCategory())
                .title("title-" + idx)
                .annotation("annotation-" + idx)
                .description("description-" + idx)
                .eventDate(LocalDateTime.now().plusDays(1))
                .locationLat((float) idx % 90)
                .locationLon((float) idx % 90)
                .paid(false)
                .requestModeration(false)
                .participantLimit(0L)
                .state(EventState.PUBLISHED)
                .confirmedRequests(0L)
                .createdOn(LocalDateTime.now().minusDays(2))
                .publishedOn(LocalDateTime.now().minusDays(1))
                .cachedViews(100500L)
                .build();
    }
}
