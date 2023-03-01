package ru.practicum.ewm.request.service;

import ru.practicum.ewm.api.dto.ParticipationRequestDto;

import java.util.List;

public interface PromoterRequestService {
    List<ParticipationRequestDto> getForEvent(long callerId, long eventId);
}
