package ru.practicum.ewm.request.service;

import ru.practicum.ewm.api.dto.ParticipationRequestDto;

public interface RequestPrivateService {
    ParticipationRequestDto add(long requesterId, long eventId);
}
