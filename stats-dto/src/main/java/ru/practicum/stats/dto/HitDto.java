package ru.practicum.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.stats.dto.annotations.IPAddress;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class HitDto {
    Long id;
    @NotNull
    String app;
    @NotNull
    String uri;
    @NotNull
    @IPAddress
    String ip;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;
}