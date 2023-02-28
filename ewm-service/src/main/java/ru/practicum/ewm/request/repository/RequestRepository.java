package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    default Request require(long id) {
        return findById(id)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Request with id=%d was not found", id))
                );
    }

    List<Request> findAllByRequesterId(long requesterId);
}