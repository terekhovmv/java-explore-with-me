package ru.practicum.ewm.request.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.ParticipationRequestDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.request.mapping.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.request.service.PromoterRequestService;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromoterRequestServiceImpl implements PromoterRequestService {
    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final RequestMapper mapper;

    @Override
    public List<ParticipationRequestDto> getForEvent(long callerId, long eventId) {
        userRepository.require(callerId);
        Event event = eventRepository.requireInitiated(eventId, callerId);

        List<Request> found = requestRepository.findAllByEventId(event.getId());

        return found
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
