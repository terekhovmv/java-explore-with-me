package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, CustomEventRepository {
    default Event require(long id) {
        return findById(id)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Event with id=%d was not found", id))
                );
    }

    default Event requireInitiated(long id, long initiatorId) {
        return findFirstByIdAndInitiatorId(id, initiatorId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Event with id=%d was not found", id))
                );
    }

    @Modifying
    @Query("UPDATE Event e SET e.confirmedRequests = e.confirmedRequests + :inc WHERE e.id = :id")
    void incrementConfirmedRequests(@Param("id") long id, @Param("inc") long inc);

    Page<Event> findAllByInitiatorId(long initiatorId, Pageable pageable);

    Optional<Event> findFirstByIdAndInitiatorId(long id, long initiatorId);

    List<Event> findAllByIdIn(List<Long> ids);

    @Query(
            "SELECT e FROM Event e JOIN FETCH e.category JOIN FETCH e.initiator " +
                    "WHERE e.id = :id AND e.state = ru.practicum.ewm.event.model.EventState.PUBLISHED"
    )
    Optional<Event> findPublishedById(@Param("id") long id);
}