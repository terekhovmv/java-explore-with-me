package ru.practicum.stats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.stats.dto.DtoFormat;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.RegisterHitDto;
import ru.practicum.stats.dto.SummaryDto;
import ru.practicum.stats.service.StatsService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {StatsController.class})
public class StatsControllerTest {
    @MockBean
    private StatsService service;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void registerHit() throws Exception {
        final LocalDateTime timestamp = LocalDateTime.now();
        final HitDto responseDto = createHitDto(timestamp);
        final RegisterHitDto dto = createRegisterHitDto(responseDto);

        when(
                service.registerHit(dto)
        ).thenReturn(responseDto);

        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.app", is(responseDto.getApp())))
                .andExpect(jsonPath("$.uri", is(responseDto.getUri())))
                .andExpect(jsonPath("$.ip", is(responseDto.getIp())))
                .andExpect(jsonPath("$.timestamp", is(dateTimeToString(responseDto.getTimestamp()))));
    }

    @Test
    void getStats() throws Exception {
        final LocalDateTime start = LocalDateTime.now().minusHours(2).truncatedTo(ChronoUnit.SECONDS);
        final LocalDateTime end = LocalDateTime.now().minusHours(1).truncatedTo(ChronoUnit.SECONDS);
        final List<String> uris = List.of("/add", "/bad", "/cut");
        final boolean unique = true;

        final SummaryDto firstSummary = createSummaryDto(1, 2);
        final SummaryDto secondSummary = createSummaryDto(2, 1);

        when(
                service.getStats(start, end, uris, unique)
        ).thenReturn(List.of(firstSummary, secondSummary));

        mvc.perform(get("/stats")
                        .param("start", dateTimeToString(start))
                        .param("end", dateTimeToString(end))
                        .param("uris", uris.toArray(new String[0]))
                        .param("unique", Boolean.toString(unique))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$.[0].app", is(firstSummary.getApp())))
                .andExpect(jsonPath("$.[0].uri", is(firstSummary.getUri())))
                .andExpect(jsonPath("$.[0].hits", is(firstSummary.getHits()), Long.class))
                .andExpect(jsonPath("$.[1].app", is(secondSummary.getApp())))
                .andExpect(jsonPath("$.[1].uri", is(secondSummary.getUri())))
                .andExpect(jsonPath("$.[1].hits", is(secondSummary.getHits()), Long.class));
    }

    private RegisterHitDto createRegisterHitDto(HitDto produced) {
        RegisterHitDto result = new RegisterHitDto();
        result.setApp(produced.getApp());
        result.setUri(produced.getUri());
        result.setIp(produced.getIp());
        result.setTimestamp(produced.getTimestamp());
        return result;
    }

    private HitDto createHitDto(LocalDateTime timestamp) {
        final long idx = 123;
        return new HitDto(
                idx,
                "app-name" + idx,
                "uri-path-" + idx,
                "127.0.0." + idx % 128,
                timestamp.truncatedTo(ChronoUnit.SECONDS)
        );
    }

    private SummaryDto createSummaryDto(int idx, long hits) {
        return new SummaryDto(
                "app-name" + idx,
                "uri-path-" + idx,
                hits
        );
    }

    private String dateTimeToString(LocalDateTime from) {
        return DateTimeFormatter.ofPattern(DtoFormat.DATE_TIME_FORMAT).format(from);
    }
}
