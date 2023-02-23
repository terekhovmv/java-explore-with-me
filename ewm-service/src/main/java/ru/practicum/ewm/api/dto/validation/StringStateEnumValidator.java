package ru.practicum.ewm.api.dto.validation;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.dto.EventFullDto;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StringStateEnumValidator {
    private static EventFullDto.StateEnum fromString(String path, String value) {
        if (value == null) {
            new ValidationException("Incorrect null value of provided " + path);
        }
        String correctedFrom = value.trim().toUpperCase();
        return Arrays.stream(EventFullDto.StateEnum.values())
                .filter(v -> v.toString().equalsIgnoreCase(correctedFrom))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Incorrect format of provided " + path));
    }

    public List<EventFullDto.StateEnum> requireValid(List<String> toValidate, String path) {
        return toValidate
                .stream()
                .map(item -> fromString(path, item))
                .collect(Collectors.toList());
    }

    public List<EventFullDto.StateEnum> requireValidOrNull(List<String> toValidate, String path) {
        if (toValidate == null) {
            return List.of();
        }

        return requireValid(toValidate, path);
    }
}
