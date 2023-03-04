package ru.practicum.ewm.api.dto.mapping;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeMapper {
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);

    public String dateTimeToString(LocalDateTime from) {
        if (from == null) {
            return null;
        }

        return formatter.format(from);
    }

    public LocalDateTime stringToDateTime(String from) {
        if (from == null) {
            return null;
        }

        return LocalDateTime.parse(from, formatter);
    }
}
