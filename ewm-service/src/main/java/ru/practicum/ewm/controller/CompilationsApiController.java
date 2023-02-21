package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.CompilationsApi;
import ru.practicum.ewm.api.model.CompilationDto;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CompilationsApiController implements CompilationsApi {
    @Override
    public ResponseEntity<CompilationDto> getCompilation(Long compId) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<List<CompilationDto>> getCompilations(Boolean pinned, Integer from, Integer size) {
        //TODO
        throw new UnsupportedOperationException();
    }
}
