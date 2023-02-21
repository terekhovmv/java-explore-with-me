package ru.practicum.ewm.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    default User require(long id) {
        return findById(id)
                .orElseThrow(
                        () -> new NotFoundException(String.format("User with id=%d was not found", id))
                );
    }

    Page<User> findByIdIn(List<Long> ids, Pageable pageable);
}

