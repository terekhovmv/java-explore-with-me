package ru.practicum.stats.model;

import lombok.Getter;

@Getter
public class Summary {
    private final short appId;
    private final long uriId;
    private final long hits;

    public Summary(short appId, long uriId, long hits) {
        this.appId = appId;
        this.uriId = uriId;
        this.hits = hits;
    }
}