package ru.practicum.ewm.event.mapping;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.exception.MappingException;

@Component
public class EventStateMapper {
    public EventFullDto.StateEnum toDtoState(EventState from) {
        switch (from) {
            case PENDING:
                return EventFullDto.StateEnum.PENDING;
            case PUBLISHED:
                return EventFullDto.StateEnum.PUBLISHED;
            case CANCELED:
                return EventFullDto.StateEnum.CANCELED;
            default:
                throw new MappingException("Unexpected DTO state: " + from);
        }
    }

    public EventState toState(EventFullDto.StateEnum from) {
        switch (from) {
            case PENDING:
                return EventState.PENDING;
            case PUBLISHED:
                return EventState.PUBLISHED;
            case CANCELED:
                return EventState.CANCELED;
            default:
                throw new MappingException("Unexpected state: " + from);
        }
    }
}
