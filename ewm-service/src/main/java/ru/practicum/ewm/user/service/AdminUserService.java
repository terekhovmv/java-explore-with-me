package ru.practicum.ewm.user.service;

import ru.practicum.ewm.api.dto.NewUserRequest;
import ru.practicum.ewm.api.dto.UserDto;

import java.util.List;

public interface AdminUserService {
    UserDto add(NewUserRequest dto);

    void remove(long id);

    List<UserDto> getByIds(List<Long> ids, int from, int size);
}
