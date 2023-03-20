package ru.practicum.ewm.event.repository.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventFilter;
import ru.practicum.ewm.event.model.EventState;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class EventFilterPredicatesBuilder {
    private final CriteriaBuilder cb;
    private final Root<Event> root;

    public List<Predicate> build(EventFilter filter) {
        PredicatesList result = new PredicatesList();

        result.buildAndAdd(this::forText, filter.getText());
        result.buildAndAdd(this::forInitiators, filter.getInitiators());
        result.buildAndAdd(this::forStates, filter.getStates());
        result.buildAndAdd(this::forCategories, filter.getCategories());
        result.add(forStart(filter.getStart(), filter.getEnd()));
        result.buildAndAdd(this::forEnd, filter.getEnd());
        result.buildAndAdd(this::forPaid, filter.getPaid());
        result.add(forOnlyAvailable(filter.isOnlyAvailable()));

        return result;
    }

    private Predicate forText(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        String pattern = "%" + value.toUpperCase() + "%";
        return cb.or(
                cb.like(cb.upper(root.get("annotation")), pattern),
                cb.like(cb.upper(root.get("description")), pattern)
        );
    }

    private Predicate forInitiators(List<Long> value) {
        return root.get("initiator").get("id").in(value);
    }

    private Predicate forStates(List<EventState> value) {
        return root.get("state").in(value);
    }

    private Predicate forCategories(List<Long> value) {
        return root.get("category").get("id").in(value);
    }

    private Predicate forStart(LocalDateTime start, LocalDateTime end) {
        Path<LocalDateTime> eventEventDate = root.get("eventDate");
        if (start == null && end == null) {
            return cb.greaterThanOrEqualTo(eventEventDate, LocalDateTime.now());
        }

        return (start != null)
                ? cb.greaterThanOrEqualTo(eventEventDate, start)
                : null;
    }

    private Predicate forEnd(LocalDateTime value) {
        return cb.lessThanOrEqualTo(root.get("eventDate"), value);
    }

    private Predicate forPaid(Boolean value) {
        return cb.equal(root.get("paid"), value);
    }

    private Predicate forOnlyAvailable(boolean value) {
        if (!value) {
            return null;
        }

        Path<Long> eventParticipantLimit = root.get("participantLimit");
        return cb.or(
                cb.lessThanOrEqualTo(eventParticipantLimit, 0L),
                cb.lessThan(root.get("confirmedRequests"), eventParticipantLimit)
        );
    }

    private static class PredicatesList extends ArrayList<Predicate> {
        public <V> void buildAndAdd(Function<V, Predicate> builder, V value) {
            if (value == null) {
                return;
            }

            add(builder.apply(value));
        }

        @Override
        public boolean add(Predicate predicate) {
            if (predicate == null) {
                return false;
            }
            return super.add(predicate);
        }
    }
}
