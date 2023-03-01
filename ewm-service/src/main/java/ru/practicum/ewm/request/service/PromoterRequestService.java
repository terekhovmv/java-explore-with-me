package ru.practicum.ewm.request.service;

import ru.practicum.ewm.api.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.api.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.api.dto.ParticipationRequestDto;

import java.util.List;

public interface PromoterRequestService {
    List<ParticipationRequestDto> getForEvent(long callerId, long eventId);

    EventRequestStatusUpdateResult changeStatuses(long callerId, long eventId, EventRequestStatusUpdateRequest dto);
}
