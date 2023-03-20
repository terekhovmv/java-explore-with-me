package ru.practicum.ewm.subscription.mapping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.dto.SubscriptionInfoDto;
import ru.practicum.ewm.subscription.model.SubscriptionInfo;
import ru.practicum.ewm.user.mapping.UserMapper;

@Component
@RequiredArgsConstructor
public class SubscriptionInfoMapper {
    private final UserMapper userMapper;

    public SubscriptionInfoDto toDto(SubscriptionInfo from) {
        return new SubscriptionInfoDto()
                .promoter(userMapper.toShortDto(from.getPromoter()))
                .subscribers(from.getSubscribers());
    }
}
