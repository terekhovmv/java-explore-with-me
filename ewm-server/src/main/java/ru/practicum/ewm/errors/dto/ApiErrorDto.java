package ru.practicum.ewm.errors.dto;

import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Value
public class ApiErrorDto {
    HttpStatus status;
    String reason;
    String message;
    List<String> errors;
    LocalDateTime timestamprr;
}
