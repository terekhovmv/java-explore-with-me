package ru.practicum.ewm.event.repository.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventFilter;
import ru.practicum.ewm.event.model.EventSort;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.CustomEventRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomEventRepositoryImpl implements CustomEventRepository {
    private final EntityManager em;

    @Override
    public List<Event> find(
            EventFilter filter,
            EventSort sort,
            int from,
            int size
    ) {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);
        Path<LocalDateTime> eventEventDate = eventRoot.get("eventDate");

        query.select(eventRoot);

        eventRoot.fetch("initiator", JoinType.LEFT);
        eventRoot.fetch("category", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotBlank(filter.getText())) {
            Path<String> eventAnnotation = eventRoot.get("annotation");
            Path<String> eventDescription = eventRoot.get("description");
            String pattern = "%" + filter.getText().toUpperCase() + "%";
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
        if (filter.getInitiators() != null) {
            Path<Long> eventInitiatorId = eventRoot.get("initiator").get("id");
            predicates.add(eventInitiatorId.in(filter.getInitiators()));
        }
        if (filter.getStates() != null) {
            Path<EventState> eventState = eventRoot.get("state");
            predicates.add(eventState.in(filter.getStates()));
        }
        if (filter.getCategories() != null) {
            Path<Long> eventCategoryId = eventRoot.get("category").get("id");
            predicates.add(eventCategoryId.in(filter.getCategories()));
        }
        if (filter.getStart() == null && filter.getEnd() == null) {
            predicates.add(builder.greaterThanOrEqualTo(eventEventDate, LocalDateTime.now()));
        } else {
            if (filter.getStart() != null) {
                predicates.add(builder.greaterThanOrEqualTo(eventEventDate, filter.getStart()));
            }
            if (filter.getEnd() != null) {
                predicates.add(builder.lessThanOrEqualTo(eventEventDate, filter.getEnd()));
            }
        }
        if (filter.getPaid() != null) {
            Path<Boolean> eventPaid = eventRoot.get("paid");
            predicates.add(builder.equal(eventPaid, filter.getPaid()));
        }
        if (filter.isOnlyAvailable()) {
            Path<Long> eventParticipantLimit = eventRoot.get("participantLimit");
            Path<Long> eventConfirmedRequests = eventRoot.get("confirmedRequests");
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

        query.where(predicates.toArray(new Predicate[0]));

        if (sort == EventSort.VIEWS) {
            Path<Long> eventCachedViews = eventRoot.get("cachedViews");
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

    @Override
    public void updateCachedViews(Map<Long, Long> viewsByIds) {
        if (viewsByIds.isEmpty()) {
            return;
        }

        StringBuilder sb = new StringBuilder("UPDATE events SET cached_views = CASE \n");
        for (Map.Entry<Long, Long> entry : viewsByIds.entrySet()) {
            sb.append(String.format("WHEN id = %d THEN %d \n", entry.getKey(), entry.getValue()));
        }
        sb.append("ELSE cached_views END \n");
        sb.append("WHERE id IN :ids");

        Query q = em.createNativeQuery(sb.toString());
        q.setParameter("ids", new ArrayList<>(viewsByIds.keySet()));
        q.executeUpdate();
    }
}
