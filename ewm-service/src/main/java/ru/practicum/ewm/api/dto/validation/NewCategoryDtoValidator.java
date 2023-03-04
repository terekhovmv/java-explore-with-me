package ru.practicum.ewm.api.dto.validation;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.dto.NewCategoryDto;

@Component
public class NewCategoryDtoValidator extends BaseDtoValidator {
    public void requireValid(NewCategoryDto toValidate) {
        requireNotBlank("name", toValidate.getName());
    }
}
