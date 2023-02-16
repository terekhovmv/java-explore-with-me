package ru.practicum.stats.service;

import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.SummaryDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    HitDto register(HitDto dto);

    List<SummaryDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
