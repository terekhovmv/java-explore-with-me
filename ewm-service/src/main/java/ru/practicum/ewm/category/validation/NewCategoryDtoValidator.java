package ru.practicum.ewm.category.validation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.dto.NewCategoryDto;

import javax.validation.ValidationException;

@Component
public class NewCategoryDtoValidator {
    public void requireValid(NewCategoryDto toValidate) {
        if (StringUtils.isBlank(toValidate.getName())) {
            throw new ValidationException("Blank name value is not supported");
        }
    }
}
