package ru.practicum.ewm.subscription.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.pagination.RandomAccessPageRequest;
import ru.practicum.ewm.subscription.model.Subscription;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SubscriptionRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionRepository testee;

    private long lastUserIdx = 0;

    @BeforeEach
    void beforeEach() {
        lastUserIdx = 0;
    }

    @Test
    void require() {
        User ann = addUser();
        User bob = addUser();
        addSubscription(ann, bob);

        Subscription result = testee.require(ann.getId(), bob.getId());
        assertEquals(ann, result.getSubscriber());
        assertEquals(bob, result.getPromoter());
    }

    @Test
    void requireThrowsException() {
        User ann = addUser();
        User bob = addUser();
        User cam = addUser();
        addSubscription(bob, cam);

        assertThrows(NotFoundException.class, () -> testee.require(bob.getId(), ann.getId()));
        assertThrows(NotFoundException.class, () -> testee.require(ann.getId(), cam.getId()));
    }

    @Test
    void findFirstBySubscriberIdAndPromoterId() {
        User ann = addUser();
        User bob = addUser();
        addSubscription(ann, bob);

        Subscription result = testee.require(ann.getId(), bob.getId());
        assertEquals(ann, result.getSubscriber());
        assertEquals(bob, result.getPromoter());

    }

    @Test
    void getSubscribed() {
        final int windowSize = 10;
        final int totalSize = windowSize * 5;

        User subscriber = addUser();
        List<User> subscribed = new ArrayList<>();
        for (int i = 0; i < totalSize; i++) {
            User user = addUser();
            subscribed.add(user);
            addSubscription(subscriber, user);
        }
        addUser();


        BiConsumer<Integer, Integer> tester = (from, expectedSize) -> {
            List<User> result = testee.getSubscribed(
                    subscriber.getId(),
                    RandomAccessPageRequest.of(from, windowSize, Sort.unsorted())
            ).getContent();

            assertIterableEquals(
                    subscribed.subList(from, from + expectedSize),
                    result
            );
        };

        tester.accept(0, windowSize);
        tester.accept(totalSize / 2, windowSize);
        tester.accept(totalSize - windowSize / 2, windowSize / 2);
    }

    @Test
    void getSubscribedEmptyList() {
        final int totalSize = 10;

        User subscriber = addUser();
        for (int i = 0; i < totalSize; i++) {
            addUser();
        }

        List<User> result = testee.getSubscribed(
                subscriber.getId(),
                RandomAccessPageRequest.of(0, totalSize * 2, Sort.unsorted())
        ).getContent();

        assertTrue(result.isEmpty());
    }

    @Test
    void getSubscribedIds() {
        final int totalSize = 10;

        User subscriber = addUser();
        List<User> subscribed = new ArrayList<>();
        for (int i = 0; i < totalSize; i++) {
            User user = addUser();
            subscribed.add(user);
            addSubscription(subscriber, user);
        }
        addUser();


        List<Long> actual = testee.getSubscribedIds(
                subscriber.getId()
        );
        List<Long> expected = subscribed.stream().map(User::getId).collect(Collectors.toList());

        assertIterableEquals(expected, actual);
    }

    @Test
    void getSubscribedIdsEmptyList() {
        final int totalSize = 10;

        User subscriber = addUser();
        for (int i = 0; i < totalSize; i++) {
            addUser();
        }

        assertTrue(testee.getSubscribedIds(subscriber.getId()).isEmpty());
    }

    private User addUser() {
        long idx = lastUserIdx++;

        String name = "user-name-" + (1000 + idx);
        User archetype = new User(
                null,
                name,
                name + "@abc.def"
        );
        return userRepository.save(archetype);
    }

    private void addSubscription(User subscriber, User promoter) {
        Subscription archetype = new Subscription(
                null,
                subscriber,
                promoter,
                null
        );
        testee.save(archetype);
    }
}
