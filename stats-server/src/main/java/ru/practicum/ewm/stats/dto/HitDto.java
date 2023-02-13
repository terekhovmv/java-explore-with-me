package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewm.stats.dto.annotations.IpAddress;

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
    @IpAddress
    String ip;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;
}
