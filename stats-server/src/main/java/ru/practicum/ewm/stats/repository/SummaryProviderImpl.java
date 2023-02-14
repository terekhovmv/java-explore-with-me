package ru.practicum.ewm.stats.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.stats.model.Hit;
import ru.practicum.ewm.stats.model.Summary;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SummaryProviderImpl implements SummaryProvider {
    private final EntityManager em;

    @Override
    public List<Summary> getSummaries(LocalDateTime start, LocalDateTime end, List<Long> uriIds, boolean uniqueIPs) {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
        Root<Hit> hit = query.from(Hit.class);
        Path hitAppId = hit.get("app").get("id");
        Path hitUriId = hit.get("uri").get("id");
        Path hitIp = hit.get("ip");
        Path hitTimestamp = hit.get("timestamp");

        Expression<Long> resultHits = uniqueIPs
                ? builder.countDistinct(hitIp)
                : builder.count(hitIp);

        query.multiselect(
                hitAppId.alias("appId"),
                hitUriId.alias("uriId"),
                resultHits.alias("hits")
        );

        List<Predicate> predicates = new ArrayList<>();
        if (start != null) {
            predicates.add(builder.greaterThanOrEqualTo(hitTimestamp, start));
        }
        if (end != null) {
            predicates.add(builder.lessThanOrEqualTo(hitTimestamp, end));
        }
        if (uriIds != null) {
            predicates.add(hitUriId.in(uriIds));
        }
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        query.groupBy(hitAppId, hitUriId);

        query.orderBy(builder.desc(resultHits));

        return em.createQuery(query)
                .getResultStream()
                .map(tuple -> new Summary(
                        tuple.get(0, Short.class),
                        tuple.get(1, Long.class),
                        tuple.get(2, Long.class)
                ))
                .collect(Collectors.toList());
    }
}
