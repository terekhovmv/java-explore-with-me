package ru.practicum.ewm.api.dto.validation;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.event.model.EventSort;

import javax.validation.ValidationException;
import java.util.Arrays;

@Component
public class StringEventSortValidator {
    public EventSort requireValidOrNull(String toValidate, String path) {
        if (toValidate == null) {
            return null;
        }

        String correctedFrom = toValidate.trim().toUpperCase();
        return Arrays.stream(EventSort.values())
                .filter(v -> v.toString().equalsIgnoreCase(correctedFrom))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Incorrect format of provided " + path));
    }
}
