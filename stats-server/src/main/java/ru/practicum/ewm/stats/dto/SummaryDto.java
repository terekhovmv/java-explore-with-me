package ru.practicum.ewm.stats.dto;

import lombok.Data;

@Data
public class SummaryDto {
    String app;
    String uri;
    long hits;
}
