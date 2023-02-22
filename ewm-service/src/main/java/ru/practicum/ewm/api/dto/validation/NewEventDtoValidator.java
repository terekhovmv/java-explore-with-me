package ru.practicum.ewm.api.dto.validation;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.dto.NewEventDto;

@Component
public class NewEventDtoValidator extends BaseDtoValidator {
    public void requireValid(NewEventDto toValidate) {
        requireSize("title", toValidate.getTitle(), 3, 100);
        requireSize("annotation", toValidate.getAnnotation(), 20, 2000);
        requireSize("description", toValidate.getDescription(), 20, 7000);
        requireDateTime("eventDate", toValidate.getEventDate());
    }
}
