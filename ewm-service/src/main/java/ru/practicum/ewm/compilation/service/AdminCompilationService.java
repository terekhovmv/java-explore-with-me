package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.api.dto.CompilationDto;
import ru.practicum.ewm.api.dto.NewCompilationDto;
import ru.practicum.ewm.api.dto.UpdateCompilationRequest;

public interface AdminCompilationService {

    CompilationDto add(NewCompilationDto dto);

    void remove(long id);

    CompilationDto update(long id, UpdateCompilationRequest dto);
}
