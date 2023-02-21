package ru.practicum.ewm.user.service;

import ru.practicum.ewm.api.model.NewUserDto;
import ru.practicum.ewm.api.model.UserDto;

import java.util.List;

public interface UserService {
    UserDto add(NewUserDto dto);

    void remove(long id);

    List<UserDto> getByIds(List<Long> ids, int from, int size);
}
