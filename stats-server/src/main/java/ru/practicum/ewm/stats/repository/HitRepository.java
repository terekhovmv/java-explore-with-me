package ru.practicum.ewm.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.stats.model.Hit;

public interface HitRepository extends JpaRepository<Hit, Long>, SummaryProvider {
}