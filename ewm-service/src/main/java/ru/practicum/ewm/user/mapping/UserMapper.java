package ru.practicum.ewm.user.mapping;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.model.NewUserDto;
import ru.practicum.ewm.api.model.UserDto;
import ru.practicum.ewm.api.model.UserShortDto;
import ru.practicum.ewm.user.model.User;

@Component
public final class UserMapper {
    public UserDto toDto(User from) {
        return new UserDto()
                .id(from.getId())
                .name(from.getName())
                .email(from.getEmail());
    }

    public UserShortDto toShortDto(User from) {
        return new UserShortDto()
                .id(from.getId())
                .name(from.getName());
    }

    public User transientFromDto(NewUserDto from) {
        return new User(
                null,
                from.getName(),
                from.getEmail()
        );
    }
}

