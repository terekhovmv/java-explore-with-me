package ru.practicum.ewm.api.dto.validation;

import org.springframework.stereotype.Component;

@Component
public class RandomAccessPageRequestValidator extends BaseDtoValidator {
    public void requireValid(Integer from, Integer size) {
        requireInRange("from", from, 0, null);
        requireInRange("size", size, 1, null);
    }
}
