package ru.practicum.stats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.service.StatsService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
        HitDto requestDto = createHitDto(idx, false, timestamp);
        HitDto responseDto = createHitDto(idx, true, timestamp);

        when(
                service.register(any())
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

    private HitDto createHitDto(long idx, boolean withId, LocalDateTime timestamp) {
        HitDto result = new HitDto();
        result.setId(withId ? idx : null);
        result.setApp("app-name" + idx);
        result.setUri("uri-path-" + idx);
        result.setIp("127.0.0." + idx % 128);
        result.setTimestamp(timestamp.truncatedTo(ChronoUnit.SECONDS));
        return result;
    }

    private String dateTimeToString(LocalDateTime from) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(from);
    }
}
