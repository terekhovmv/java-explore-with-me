package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.model.NewUserRequest;
import ru.practicum.ewm.api.model.UserDto;
import ru.practicum.ewm.pagination.RandomAccessPageRequest;
import ru.practicum.ewm.user.mapping.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    private final UserMapper mapper;

    @Override
    public UserDto add(NewUserRequest dto) {
        User created = repository.save(
                mapper.transientFromDto(dto)
        );
        log.info("User '{}' was successfully added with id {}", created.getName(), created.getId());
        return mapper.toDto(created);
    }

    @Override
    public void remove(long id) {
        repository.require(id);
        repository.deleteById(id);
        log.info("User #'{}' was successfully removed", id);
    }

    @Override
    public List<UserDto> getByIds(List<Long> ids, int from, int size) {
        return repository
                .findByIdIn(
                        ids,
                        RandomAccessPageRequest.of(
                                from,
                                size,
                                Sort.by(Sort.Direction.ASC, "id")
                        )
                )
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
