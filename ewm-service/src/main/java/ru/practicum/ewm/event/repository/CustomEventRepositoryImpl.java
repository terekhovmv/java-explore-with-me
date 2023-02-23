package ru.practicum.ewm.event.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomEventRepositoryImpl implements CustomEventRepository {
    private final EntityManager em;

    @Override
    public List<Event> find(List<Long> filterUsers, List<EventState> filterStates, List<Long> filterCategories, LocalDateTime filterStart, LocalDateTime filterEnd, int from, int size) {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);

        Path<Long> eventInitiatorId = eventRoot.get("initiator").get("id");
        Path<EventState> eventState = eventRoot.get("state");
        Path<Long> eventCategoryId = eventRoot.get("category").get("id");
        Path<LocalDateTime> eventEventDate = eventRoot.get("eventDate");

        query.select(eventRoot);

        eventRoot.fetch("initiator", JoinType.LEFT);
        eventRoot.fetch("category", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        if (filterUsers != null) {
            predicates.add(eventInitiatorId.in(filterUsers));
        }
        if (filterStates != null) {
            predicates.add(eventState.in(filterStates));
        }
        if (filterCategories != null) {
            predicates.add(eventCategoryId.in(filterCategories));
        }
        if (filterStart != null) {
            predicates.add(builder.greaterThanOrEqualTo(eventEventDate, filterStart));
        }
        if (filterEnd != null) {
            predicates.add(builder.lessThanOrEqualTo(eventEventDate, filterEnd));
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        query.orderBy(builder.asc(eventEventDate));

        return em.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultStream()
                .collect(Collectors.toList());
    }
}
