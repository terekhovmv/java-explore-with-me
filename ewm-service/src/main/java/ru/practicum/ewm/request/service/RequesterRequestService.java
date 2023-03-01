package ru.practicum.ewm.request.service;

import ru.practicum.ewm.api.dto.ParticipationRequestDto;

import java.util.List;

public interface RequesterRequestService {
    ParticipationRequestDto add(long callerId, long eventId);

    List<ParticipationRequestDto> getRequested(long callerId);

    ParticipationRequestDto cancel(long callerId, long requestId);
}
