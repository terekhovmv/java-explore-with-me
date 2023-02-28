package ru.practicum.ewm.event.repository.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventSort;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.CustomEventRepository;

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
    public List<Event> find(
            List<Long> filterUsers,
            List<EventState> filterStates,
            List<Long> filterCategories,
            LocalDateTime filterStart,
            LocalDateTime filterEnd,
            int from,
            int size
    ) {
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

    @Override
    public List<Event> find(
            String filterText,
            List<Long> filterCategories,
            Boolean filterPaid,
            LocalDateTime filterStart,
            LocalDateTime filterEnd,
            boolean filterOnlyAvailable,
            EventSort sort,
            int from,
            int size
    ) {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);


        Path<String> eventAnnotation = eventRoot.get("annotation");
        Path<String> eventDescription = eventRoot.get("description");
        Path<Long> eventCategoryId = eventRoot.get("category").get("id");
        Path<Boolean> eventPaid = eventRoot.get("paid");
        Path<LocalDateTime> eventEventDate = eventRoot.get("eventDate");
        Path<Long> eventParticipantLimit = eventRoot.get("participantLimit");
        Path<Long> eventConfirmedRequests = eventRoot.get("confirmedRequests");
        Path<Long> eventCachedViews = eventRoot.get("cachedViews");

        query.select(eventRoot);

        eventRoot.fetch("initiator", JoinType.LEFT);
        eventRoot.fetch("category", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotBlank(filterText)) {
            String pattern = "%" + filterText.toUpperCase() + "%";
            predicates.add(builder.or(
                    builder.like(
                            builder.upper(eventAnnotation),
                            pattern
                    ),
                    builder.like(
                            builder.upper(eventDescription),
                            pattern
                    )
            ));
        }
        if (filterCategories != null) {
            predicates.add(eventCategoryId.in(filterCategories));
        }
        if (filterPaid != null) {
            predicates.add(builder.equal(eventPaid, filterPaid));
        }
        if (filterStart == null && filterEnd == null) {
            predicates.add(builder.greaterThanOrEqualTo(eventEventDate, LocalDateTime.now()));
        } else {
            if (filterStart != null) {
                predicates.add(builder.greaterThanOrEqualTo(eventEventDate, filterStart));
            }
            if (filterEnd != null) {
                predicates.add(builder.lessThanOrEqualTo(eventEventDate, filterEnd));
            }
        }
        if (filterOnlyAvailable) {
            predicates.add(builder.or(
                    builder.lessThanOrEqualTo(
                            eventParticipantLimit,
                            0L
                    ),
                    builder.lessThan(
                            eventConfirmedRequests,
                            eventParticipantLimit
                    )
            ));
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        if (sort == EventSort.VIEWS) {
            query.orderBy(builder.desc(eventCachedViews));
        } else {
            query.orderBy(builder.asc(eventEventDate));
        }

        return em.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultStream()
                .collect(Collectors.toList());
    }
}
