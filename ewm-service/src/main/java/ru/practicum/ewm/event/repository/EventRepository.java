package ru.practicum.ewm.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.NotFoundException;

public interface EventRepository extends JpaRepository<Event, Long> {
    default Event require(long id) {
        return findById(id)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Event with id=%d was not found", id))
                );
    }
}