package ru.practicum.stats.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.DtoFormat;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.RegisterHitDto;
import ru.practicum.stats.dto.SummaryDto;
import ru.practicum.stats.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class StatsController {
    private final StatsService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    HitDto registerHit(@RequestBody @Valid RegisterHitDto dto) {
        return service.registerHit(dto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    List<SummaryDto> getStats(
            @RequestParam @DateTimeFormat(pattern = DtoFormat.DATE_TIME_FORMAT) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = DtoFormat.DATE_TIME_FORMAT) LocalDateTime end,
            @RequestParam List<String> uris,
            @RequestParam(required = false, defaultValue = "false") boolean unique
    ) {
        return service.getStats(start, end, uris, unique);
    }

}