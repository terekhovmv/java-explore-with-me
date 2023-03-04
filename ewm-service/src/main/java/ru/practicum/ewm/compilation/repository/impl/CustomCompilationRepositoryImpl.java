package ru.practicum.ewm.compilation.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CustomCompilationRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class CustomCompilationRepositoryImpl implements CustomCompilationRepository {

    private final EntityManager em;

    @Override
    public List<Compilation> find(Boolean pinned, int from, int size) {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<Compilation> query = builder.createQuery(Compilation.class);
        Root<Compilation> compilationRoot = query.from(Compilation.class);

        Path<Boolean> compilationPinned = compilationRoot.get("pinned");
        Path<Long> compilationId = compilationRoot.get("id");

        query.select(compilationRoot);

        List<Predicate> predicates = new ArrayList<>();
        if (pinned != null) {
            predicates.add(builder.equal(compilationPinned, pinned));
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        query.orderBy(builder.asc(compilationId));

        return em.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultStream()
                .collect(Collectors.toList());
    }
}
