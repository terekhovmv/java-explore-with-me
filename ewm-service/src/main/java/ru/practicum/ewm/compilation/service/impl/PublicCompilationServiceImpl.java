package ru.practicum.ewm.compilation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.CompilationDto;
import ru.practicum.ewm.compilation.mapping.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.compilation.service.PublicCompilationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationRepository repository;

    private final CompilationMapper mapper;

    @Override
    public CompilationDto get(long id) {
        Compilation found = repository.require(id);
        return mapper.toDto(found);
    }

    @Override
    public List<CompilationDto> find(Boolean pinned, int from, int size) {
        return repository
                .find(pinned, from, size)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
