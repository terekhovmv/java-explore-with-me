package ru.practicum.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.stats.dto.annotations.IPAddress;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class RegisterHitDto {
    @NotBlank
    String app;
    @NotBlank
    String uri;
    @NotBlank
    @IPAddress
    String ip;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoFormat.DATE_TIME_FORMAT)
    LocalDateTime timestamp;
}
