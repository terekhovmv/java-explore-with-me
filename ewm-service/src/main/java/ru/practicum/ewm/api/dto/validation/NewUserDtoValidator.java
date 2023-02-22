package ru.practicum.ewm.api.dto.validation;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.dto.NewUserDto;

@Component
public class NewUserDtoValidator extends BaseDtoValidator {
    public void requireValid(NewUserDto toValidate) {
        requireNotBlank("name", toValidate.getName());
        requireEmail("email", toValidate.getEmail());
    }
}
