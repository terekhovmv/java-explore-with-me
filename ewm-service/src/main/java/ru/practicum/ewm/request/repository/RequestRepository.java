package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    default Request require(long id) {
        return findById(id)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Request with id=%d was not found", id))
                );
    }

    default Request requireRequested(long id, long requesterId) {
        return findFirstByIdAndRequesterId(id, requesterId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Request with id=%d was not found", id))
                );
    }

    Optional<Request> findFirstByIdAndRequesterId(long id, long requesterId);

    List<Request> findAllByRequesterId(long requesterId);

    List<Request> findAllByEventId(long eventId);
}