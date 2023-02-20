package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.CompilationsApi;

@Controller
@RequiredArgsConstructor
public class CompilationsApiController implements CompilationsApi {
    /*
    TODO:
    ResponseEntity<CompilationDto> getCompilation(Long compId);
    ResponseEntity<List<CompilationDto>> getCompilations(Boolean pinned, Integer from, Integer size);
    */
}
