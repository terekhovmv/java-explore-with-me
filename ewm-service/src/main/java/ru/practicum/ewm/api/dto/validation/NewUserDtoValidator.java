package ru.practicum.ewm.api.dto.validation;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.dto.NewUserDto;

import javax.validation.ValidationException;

@Component
public class NewUserDtoValidator {

    public void requireValid(NewUserDto toValidate) {
        if (StringUtils.isBlank(toValidate.getName())) {
            throw new ValidationException("Blank name value is not supported");
        }

        if (!EmailValidator.getInstance().isValid(toValidate.getEmail())) {
            throw new ValidationException("Email format is not valid");
        }
    }
}
