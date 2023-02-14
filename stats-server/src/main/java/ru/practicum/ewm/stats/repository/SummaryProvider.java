package ru.practicum.ewm.stats.repository;

import ru.practicum.ewm.stats.model.Summary;

import java.time.LocalDateTime;
import java.util.List;

public interface SummaryProvider {
    List<Summary> getSummaries(LocalDateTime start, LocalDateTime end, List<Long> uriIds, boolean uniqueIPs);
}
