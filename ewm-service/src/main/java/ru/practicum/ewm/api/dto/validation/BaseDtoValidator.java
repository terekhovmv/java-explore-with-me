package ru.practicum.ewm.api.dto.validation;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.DateValidator;
import org.apache.commons.validator.routines.EmailValidator;
import ru.practicum.ewm.api.dto.mapping.DateTimeMapper;

import javax.validation.ValidationException;

public class BaseDtoValidator {
    protected void requireNotBlank(String path, String value) {
        if (isNotBlank(value)) {
            return;
        }

        throw new ValidationException("Unsupported blank value of provided " + path);
    }

    protected void requireNotBlankOrNull(String path, String value) {
        if (value != null) {
            requireNotBlank(path, value);
        }
    }

    protected void requireSize(String path, String value, Integer min, Integer max) {
        if (hasSize(value, min, max)) {
            return;
        }

        throw new ValidationException("Unsupported size of provided " + path);
    }

    protected void requireSizeOrNull(String path, String value, Integer min, Integer max) {
        if (value != null) {
            requireSize(path, value, min, max);
        }
    }

    protected void requireDateTime(String path, String value) {
        if (isDateTime(value)) {
            return;
        }

        throw new ValidationException("Incorrect date/time format of provided " + path);
    }

    protected void requireDateTimeOrNull(String path, String value) {
        if (value != null) {
            requireDateTime(path, value);
        }
    }

    protected void requireEmail(String path, String value) {
        if (isEmail(value)) {
            return;
        }

        throw new ValidationException("Incorrect e-mail format of provided " + path);
    }

    protected void requireEmailOrNull(String path, String value) {
        if (value != null) {
            requireEmail(path, value);
        }
    }

    protected void requireInRange(String path, Integer value, Integer min, Integer max) {
        if (inRange(value, min, max)) {
            return;
        }

        throw new ValidationException("Unsupported value of provided " + path);
    }

    protected void requireInRangeOrNull(String path, Integer value, Integer min, Integer max) {
        if (value != null) {
            requireInRange(path, value, min, max);
        }
    }

    protected boolean isNotBlank(String value) {
        return StringUtils.isNotBlank(value);
    }

    protected boolean hasSize(String value, Integer min, Integer max) {
        return (
                (min == null) || (value.length() >= min)
        ) && (
                (max == null) || (value.length() <= max)
        );
    }

    protected boolean isDateTime(String value) {
        return DateValidator.getInstance().isValid(value, DateTimeMapper.PATTERN);
    }

    protected boolean isEmail(String value) {
        return EmailValidator.getInstance().isValid(value);
    }

    protected boolean inRange(int value, Integer min, Integer max) {
        return (
                (min == null) || (value >= min)
        ) && (
                (max == null) || (value <= max)
        );
    }
}
