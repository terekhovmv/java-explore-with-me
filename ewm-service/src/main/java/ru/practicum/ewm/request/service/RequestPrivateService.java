package ru.practicum.ewm.request.service;

import ru.practicum.ewm.api.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestPrivateService {
    ParticipationRequestDto add(long requesterId, long eventId);

    List<ParticipationRequestDto> getByRequester(long requesterId);

    List<ParticipationRequestDto> getByEvent(long callerId, long eventId);

    ParticipationRequestDto cancel(long callerId, long requestId);
}
