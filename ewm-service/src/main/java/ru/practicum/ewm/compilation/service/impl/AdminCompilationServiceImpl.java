package ru.practicum.ewm.compilation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.CompilationDto;
import ru.practicum.ewm.api.dto.NewCompilationDto;
import ru.practicum.ewm.api.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.mapping.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.compilation.service.AdminCompilationService;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    private final CompilationMapper mapper;

    @Override
    public CompilationDto add(NewCompilationDto dto) {
        List<Event> events = eventRepository.findAllByIdIn(dto.getEvents());
        Compilation created = compilationRepository.save(
                mapper.transientFromDto(dto, Set.copyOf(events))
        );
        log.info("Compilation '{}' was successfully added with id {}", created.getTitle(), created.getId());
        return mapper.toDto(created);
    }

    @Override
    public void remove(long id) {
        compilationRepository.require(id);
        compilationRepository.deleteById(id);
        log.info("Compilation #{} was successfully removed", id);
    }

    @Override
    public CompilationDto update(long id, UpdateCompilationRequest dto) {
        Compilation toUpdate = compilationRepository.require(id);
        if (dto.isPinned() != null) {
            toUpdate.setPinned(dto.isPinned());
        }
        if (dto.getTitle() != null) {
            toUpdate.setTitle(dto.getTitle());
        }
        if (dto.getEvents() != null) {
            List<Event> newEvents = eventRepository.findAllByIdIn(dto.getEvents());
            Map<Long, Event> newEventByIds = newEvents.stream().collect(Collectors.toMap(Event::getId, event -> event));

            List<Event> oldEventsToRemove = new ArrayList<>();
            for (Event oldEvent : toUpdate.getEvents()) {
                Event newEvent = newEventByIds.get(oldEvent.getId());
                if (newEvent == null) {
                    oldEventsToRemove.add(oldEvent);
                } else {
                    newEventByIds.remove(oldEvent.getId());
                }
            }
            for (Event toRemove : oldEventsToRemove) {
                toUpdate.getEvents().remove(toRemove);
            }
            for (Event newEvent : newEventByIds.values()) {
                toUpdate.getEvents().add(newEvent);
            }
        }

        Compilation updated = compilationRepository.save(toUpdate);
        log.info("Compilation #{} was successfully updated", id);

        return mapper.toDto(updated);
    }
}
