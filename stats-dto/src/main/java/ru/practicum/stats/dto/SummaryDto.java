package ru.practicum.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryDto {
    String app;
    String uri;
    long hits;
}
