package ru.practicum.ewm.subscription.repository;

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

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SubscriptionRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionRepository testee;

    @Test
    void require() {
        User ann = addUser(0);
        User bob = addUser(1);
        addSubscription(ann, bob);

        Subscription result = testee.require(ann.getId(), bob.getId());
        assertEquals(ann, result.getSubscriber());
        assertEquals(bob, result.getPromoter());
    }

    @Test
    void requireThrowsException() {
        User ann = addUser(0);
        User bob = addUser(1);
        User cam = addUser(2);
        addSubscription(bob, cam);

        assertThrows(NotFoundException.class, () -> testee.require(bob.getId(), ann.getId()));
        assertThrows(NotFoundException.class, () -> testee.require(ann.getId(), cam.getId()));
    }

    @Test
    void findFirstBySubscriberIdAndPromoterId() {
        User ann = addUser(0);
        User bob = addUser(1);
        addSubscription(ann, bob);

        Subscription result = testee.require(ann.getId(), bob.getId());
        assertEquals(ann, result.getSubscriber());
        assertEquals(bob, result.getPromoter());

    }

    @Test
    void getSubscribed() {
        final int WINDOW_SIZE = 10;
        final int TOTAL_SIZE = WINDOW_SIZE * 5;

        User subscriber = addUser(TOTAL_SIZE);
        List<User> subscribed = new ArrayList<>();
        for (int i = 0; i < TOTAL_SIZE; i++) {
            User user = addUser(i);
            subscribed.add(user);
            addSubscription(subscriber, user);
        }
        addUser(TOTAL_SIZE + 1);


        BiConsumer<Integer, Integer> tester = (from, expectedSize) -> {
            List<User> result = testee.getSubscribed(
                    subscriber.getId(),
                    RandomAccessPageRequest.of(from, WINDOW_SIZE, Sort.unsorted())
            ).getContent();

            assertIterableEquals(
                    subscribed.subList(from, from + expectedSize),
                    result
            );
        };

        tester.accept(0, WINDOW_SIZE);
        tester.accept(TOTAL_SIZE / 2, WINDOW_SIZE);
        tester.accept(TOTAL_SIZE - WINDOW_SIZE / 2, WINDOW_SIZE / 2);
    }

    @Test
    void getSubscribedEmptyList() {
        final int TOTAL_SIZE = 100;

        User subscriber = addUser(TOTAL_SIZE);
        for (int i = 0; i < TOTAL_SIZE; i++) {
            addUser(i);
        }

        List<User> result = testee.getSubscribed(
                subscriber.getId(),
                RandomAccessPageRequest.of(0, TOTAL_SIZE * 2, Sort.unsorted())
        ).getContent();

        assertTrue(result.isEmpty());
    }

    private User addUser(long idx) {
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
