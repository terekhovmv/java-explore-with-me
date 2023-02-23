package ru.practicum.ewm.api.dto.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.dto.mapping.DateTimeMapper;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class StringDateTimeValidator extends BaseDtoValidator {

    private final DateTimeMapper dateTimeMapper;

    public LocalDateTime requireValid(String toValidate, String path) {
        requireDateTime(path, toValidate);
        return dateTimeMapper.stringToDateTime(toValidate);
    }

    public LocalDateTime requireValidOrNull(String toValidate, String path) {
        requireDateTimeOrNull(path, toValidate);
        return dateTimeMapper.stringToDateTime(toValidate);
    }
}