package ru.practicum.stats.repository;

import ru.practicum.stats.model.Summary;

import java.time.LocalDateTime;
import java.util.List;

public interface SummaryProvider {
    List<Summary> getSummaries(LocalDateTime start, LocalDateTime end, List<Long> uriIds, boolean uniqueIPs);
}
