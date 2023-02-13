package ru.practicum.ewm.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.stats.dto.HitDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;

    @PostMapping(value = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    HitDto register(@RequestBody @Valid HitDto dto) {
        return service.register(dto);
    }
}