package ru.practicum.ewm.api.dto.validation;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.dto.NewUserRequest;

@Component
public class NewUserRequestValidator extends BaseDtoValidator {
    public void requireValid(NewUserRequest toValidate) {
        requireNotBlank("name", toValidate.getName());
        requireEmail("email", toValidate.getEmail());
    }
}
