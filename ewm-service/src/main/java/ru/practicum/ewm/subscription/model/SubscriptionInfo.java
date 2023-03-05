package ru.practicum.ewm.subscription.model;

import lombok.Value;
import ru.practicum.ewm.user.model.User;

@Value
public class SubscriptionInfo {
    User promoter;
    long subscribers;
}
