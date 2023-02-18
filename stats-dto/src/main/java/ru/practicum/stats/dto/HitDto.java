package ru.practicum.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class HitDto {
    Long id;
    String app;
    String uri;
    String ip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoFormat.DATE_TIME_FORMAT)
    LocalDateTime timestamp;
}
