package ru.practicum.stats.model;

import lombok.Value;

@Value
public class Summary {
    short appId;
    long uriId;
    long hits;
}
