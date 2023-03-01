package ru.practicum.ewm.request.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.api.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.api.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.api.dto.ParticipationRequestDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.request.mapping.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
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

    @Override
    @Transactional
    public EventRequestStatusUpdateResult changeStatuses(long callerId, long eventId, EventRequestStatusUpdateRequest dto) {
        userRepository.require(callerId);
        Event event = eventRepository.requireInitiated(eventId, callerId);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return new EventRequestStatusUpdateResult();
        }

        List<Request> requests = requestRepository.findAllByEventIdAndIdIn(eventId, dto.getRequestIds());
        requests.forEach(request -> {
            if (request.getStatus() != RequestStatus.PENDING) {
                throw new ConflictException(String.format("Request #%d is not in pending state", request.getId()));
            }
        });
        List<Long> requestIds = requests.stream().map(Request::getId).collect(Collectors.toList());

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        if (dto.getStatus() == EventRequestStatusUpdateRequest.StatusEnum.CONFIRMED) {
            if (event.getConfirmedRequests() + requests.size() > event.getParticipantLimit()) {
                throw new ConflictException("Not enough participant slots available");
            }

            eventRepository.incrementConfirmedRequests(eventId, requests.size());
            requests.forEach(request -> {
                request.setStatus(RequestStatus.CONFIRMED);
                requestRepository.save(request);
                result.addConfirmedRequestsItem(mapper.toDto(request));
            });
        } else if (dto.getStatus() == EventRequestStatusUpdateRequest.StatusEnum.REJECTED) {
            requests.forEach(request -> {
                request.setStatus(RequestStatus.REJECTED);
                requestRepository.save(request);
                result.addRejectedRequestsItem(mapper.toDto(request));
            });
        }
        return result;
    }
}
