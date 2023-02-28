package ru.practicum.ewm.request.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.ParticipationRequestDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.request.mapping.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.request.service.RequestPrivateService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestPrivateServiceImpl implements RequestPrivateService {

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final RequestMapper mapper;

    @Override
    @Transactional
    public ParticipationRequestDto add(long requesterId, long eventId) {
        User requester = userRepository.require(requesterId);
        Event event = eventRepository.require(eventId);

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("The event is not published");
        }

        long confirmedRequests = event.getConfirmedRequests();
        long participantLimit = event.getParticipantLimit();
        if (participantLimit != 0 && confirmedRequests >= participantLimit) {
            throw new ConflictException("Participant limit exceeded");
        }

        if (event.getInitiator().getId() == requesterId) {
            throw new ConflictException("Unable to register the request for the initiated event");
        }

        if (!event.getRequestModeration()) {
            eventRepository.incrementConfirmedRequests(eventId);
        }

        Request created = requestRepository.save(
                new Request(
                        null,
                        event,
                        requester,
                        event.getRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED,
                        null
                )
        );

        log.info(
                "Request from user #{} to event #{} was successfully registered with id {}",
                requesterId,
                eventId,
                created.getId()
        );
        return mapper.toDto(created);
    }
}
