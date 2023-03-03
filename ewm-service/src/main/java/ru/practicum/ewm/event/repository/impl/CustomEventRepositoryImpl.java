package ru.practicum.ewm.event.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventFilter;
import ru.practicum.ewm.event.model.EventSort;
import ru.practicum.ewm.event.repository.CustomEventRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
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
        Root<Event> root = query.from(Event.class);

        query.select(root);

        root.fetch("initiator", JoinType.LEFT);
        root.fetch("category", JoinType.LEFT);

        List<Predicate> predicates = new EventFilterPredicatesBuilder(builder, root).build(filter);
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        if (sort == EventSort.VIEWS) {
            query.orderBy(builder.desc(root.get("cachedViews")));
        } else {
            query.orderBy(builder.asc(root.get("eventDate")));
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
