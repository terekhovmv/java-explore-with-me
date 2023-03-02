package ru.practicum.ewm.api.dto.validation;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.dto.UpdateEventUserRequest;

@Component
public class UpdateEventUserRequestValidator extends BaseDtoValidator {
    public void requireValid(UpdateEventUserRequest toValidate) {
        requireSizeOrNull("title", toValidate.getTitle(), 3, 100);
        requireSizeOrNull("annotation", toValidate.getAnnotation(), 20, 2000);
        requireSizeOrNull("description", toValidate.getDescription(), 20, 7000);
        requireDateTimeOrNull("eventDate", toValidate.getEventDate());
    }
}
