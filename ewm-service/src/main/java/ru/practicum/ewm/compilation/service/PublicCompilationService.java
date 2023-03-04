package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.api.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {

    CompilationDto get(long id);

    List<CompilationDto> find(Boolean pinned, int from, int size);
}
