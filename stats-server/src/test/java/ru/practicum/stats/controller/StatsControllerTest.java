package ru.practicum.stats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.stats.dto.HitDto;
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
    void register() throws Exception {
        final long idx = 123;
        final LocalDateTime timestamp = LocalDateTime.now();
        final HitDto requestDto = createHitDto(idx, false, timestamp);
        final HitDto responseDto = createHitDto(idx, true, timestamp);

        when(
                service.register(requestDto)
        ).thenReturn(responseDto);

        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(requestDto))
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
                        .param("unique", new Boolean(unique).toString())
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

    private HitDto createHitDto(long idx, boolean withId, LocalDateTime timestamp) {
        HitDto result = new HitDto();
        result.setId(withId ? idx : null);
        result.setApp("app-name" + idx);
        result.setUri("uri-path-" + idx);
        result.setIp("127.0.0." + idx % 128);
        result.setTimestamp(timestamp.truncatedTo(ChronoUnit.SECONDS));
        return result;
    }

    private SummaryDto createSummaryDto(int idx, long hits) {
        SummaryDto result = new SummaryDto();
        result.setApp("app-name" + idx);
        result.setUri("uri-path-" + idx);
        result.setHits(hits);
        return result;
    }

    private String dateTimeToString(LocalDateTime from) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(from);
    }
}
