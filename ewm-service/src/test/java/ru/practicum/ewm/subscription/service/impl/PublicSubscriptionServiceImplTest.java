package ru.practicum.ewm.subscription.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.subscription.mapping.SubscriptionInfoMapper;
import ru.practicum.ewm.subscription.model.SubscriptionInfo;
import ru.practicum.ewm.subscription.repository.SubscriptionRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PublicSubscriptionServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private PublicSubscriptionServiceImpl testee;

    @Autowired
    private SubscriptionInfoMapper subscriptionInfoMapper;

    private long lastUserIdx = 0;

    @BeforeEach
    void beforeEach() {
        lastUserIdx = 0;
    }

    @Test
    void getInfo() {
        User promoter = createUser();
        final long subscribers = 123;

        when(
                userRepository.require(promoter.getId())
        ).thenReturn(promoter);
        when(
                subscriptionRepository.countSubscribers(promoter.getId())
        ).thenReturn(subscribers);

        assertEquals(
                subscriptionInfoMapper.toDto(new SubscriptionInfo(promoter, subscribers)),
                testee.getInfo(promoter.getId())
        );
    }

    @Test
    void getTopInfos() {
        final int from = 20;
        final int size = 10;
        List<SubscriptionInfo> infos = List.of(
                new SubscriptionInfo(createUser(), 1000),
                new SubscriptionInfo(createUser(), 100),
                new SubscriptionInfo(createUser(), 10),
                new SubscriptionInfo(createUser(), 1)
        );

        when(
                subscriptionRepository.getTopInfos(any())
        ).thenAnswer(invocationOnMock -> {
            Pageable pageable = invocationOnMock.getArgument(0, Pageable.class);

            assertEquals(from, pageable.getOffset());
            assertEquals(size, pageable.getPageSize());

            return new PageImpl<>(infos);
        });


        assertIterableEquals(
                infos.stream().map(subscriptionInfoMapper::toDto).collect(Collectors.toList()),
                testee.getTopInfos(from, size)
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
}
