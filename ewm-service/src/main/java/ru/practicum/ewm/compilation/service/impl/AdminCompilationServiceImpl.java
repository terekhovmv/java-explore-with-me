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

import java.util.List;
import java.util.Set;

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
        //TODO
    }

    @Override
    public CompilationDto update(long id, UpdateCompilationRequest dto) {
        //TODO
        return null;
    }
}
