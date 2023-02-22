package ru.practicum.ewm.request.mapping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.dto.ParticipationRequestDto;
import ru.practicum.ewm.api.dto.mapping.DateTimeMapper;
import ru.practicum.ewm.request.model.Request;

@Component
@RequiredArgsConstructor
public class RequestMapper {

    private final DateTimeMapper dateTimeMapper;

    public ParticipationRequestDto toDto(Request from) {
        return new ParticipationRequestDto()
                .id(from.getId())
                .event(from.getEvent().getId())
                .requester(from.getEvent().getId())
                .status(from.getStatus().toString())
                .created(dateTimeMapper.dateTimeToString(from.getCreatedOn()));
    }
}
