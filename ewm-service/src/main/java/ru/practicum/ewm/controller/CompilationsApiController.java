package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.CompilationsApi;
import ru.practicum.ewm.api.dto.CompilationDto;
import ru.practicum.ewm.api.dto.validation.RandomAccessPageRequestValidator;
import ru.practicum.ewm.compilation.service.PublicCompilationService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CompilationsApiController implements CompilationsApi {

    private final PublicCompilationService service;

    private final RandomAccessPageRequestValidator randomAccessPageRequestValidator;

    @Override
    public ResponseEntity<CompilationDto> getCompilation(Long compId) {
        return new ResponseEntity<>(
                service.get(compId),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<CompilationDto>> getCompilations(Boolean pinned, Integer from, Integer size) {
        randomAccessPageRequestValidator.requireValid(from, size);

        return new ResponseEntity<>(
                service.find(pinned, from, size),
                HttpStatus.OK
        );
    }
}
