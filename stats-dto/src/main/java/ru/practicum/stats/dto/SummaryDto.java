package ru.practicum.stats.dto;

import lombok.Value;


@Value
public class SummaryDto {
    String app;
    String uri;
    long hits;
}
