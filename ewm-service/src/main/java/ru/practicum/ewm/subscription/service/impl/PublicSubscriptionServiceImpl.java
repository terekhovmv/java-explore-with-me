package ru.practicum.ewm.subscription.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.SubscriptionInfoDto;
import ru.practicum.ewm.pagination.RandomAccessPageRequest;
import ru.practicum.ewm.subscription.mapping.SubscriptionInfoMapper;
import ru.practicum.ewm.subscription.model.SubscriptionInfo;
import ru.practicum.ewm.subscription.repository.SubscriptionRepository;
import ru.practicum.ewm.subscription.service.PublicSubscriptionService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class PublicSubscriptionServiceImpl implements PublicSubscriptionService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    private final SubscriptionInfoMapper subscriptionInfoMapper;

    @Override
    public SubscriptionInfoDto getInfo(long promoterId) {
        User promoter = userRepository.require(promoterId);

        SubscriptionInfo info = new SubscriptionInfo(
                promoter,
                subscriptionRepository.countSubscribers(promoterId)
        );
        return subscriptionInfoMapper.toDto(info);
    }

    @Override
    public List<SubscriptionInfoDto> getTopInfos(int from, int size) {
        List<SubscriptionInfo> infos = subscriptionRepository.getTopInfos(
                RandomAccessPageRequest.of(
                        from,
                        size,
                        Sort.unsorted()
                )
        ).getContent();

        return infos
                .stream()
                .map(subscriptionInfoMapper::toDto)
                .collect(Collectors.toList());
    }
}
