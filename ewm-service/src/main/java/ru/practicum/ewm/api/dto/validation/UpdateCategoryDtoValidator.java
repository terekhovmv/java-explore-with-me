package ru.practicum.ewm.api.dto.validation;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.dto.CategoryDto;

@Component
public class UpdateCategoryDtoValidator extends BaseDtoValidator {
    public void requireValid(CategoryDto toValidate) {
        requireNotBlankOrNull("name", toValidate.getName());
    }
}
