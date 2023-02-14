package ru.practicum.ewm.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.HitDto;
import ru.practicum.ewm.stats.dto.SummaryDto;

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
    HitDto register(@RequestBody @Valid HitDto dto) {
        return service.register(dto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    List<SummaryDto> getStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam List<String> uris,
            @RequestParam(required = false, defaultValue = "false") boolean unique
    ) {
        return service.getStats(start, end, uris, unique);
    }

}