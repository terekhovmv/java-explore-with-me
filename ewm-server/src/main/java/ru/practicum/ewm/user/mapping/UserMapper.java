package ru.practicum.ewm.user.mapping;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.model.UserDto;
import ru.practicum.ewm.user.model.User;

@Component
public final class UserMapper {
    public UserDto toDto(User from) {
        return new UserDto()
                .id(from.getId())
                .name(from.getName())
                .email(from.getEmail());
    }
}

