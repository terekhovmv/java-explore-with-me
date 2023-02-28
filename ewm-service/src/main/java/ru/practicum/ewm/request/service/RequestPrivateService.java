package ru.practicum.ewm.request.service;

import ru.practicum.ewm.api.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestPrivateService {
    ParticipationRequestDto add(long requesterId, long eventId);

    List<ParticipationRequestDto> getMany(long requesterId);
}
