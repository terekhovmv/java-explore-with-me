package ru.practicum.ewm.stats;

import ru.practicum.ewm.stats.dto.HitDto;

public interface StatsService {
    HitDto register(HitDto dto);
}
