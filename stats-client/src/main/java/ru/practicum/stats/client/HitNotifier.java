package ru.practicum.stats.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.stats.dto.HitDto;

import java.time.LocalDateTime;

@Slf4j
@Component
public class HitNotifier {

    private final WebClient client;
    private final String appName;

    public HitNotifier(
            @Value("${stats-server.url}") String serverUrl,
            @Value("${stats-server.app-name}") String appName,
            ObjectMapper mapper
    ) {
        ExchangeStrategies strategies = ExchangeStrategies
                .builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().jackson2JsonEncoder(
                            new Jackson2JsonEncoder(mapper, MediaType.APPLICATION_JSON)
                    );
                    configurer.defaultCodecs().jackson2JsonDecoder(
                            new Jackson2JsonDecoder(mapper, MediaType.APPLICATION_JSON)
                    );
                })
                .build();

        this.client = WebClient
                .builder()
                .baseUrl(serverUrl)
                .exchangeStrategies(strategies)
                .build();

        this.appName = appName;
    }

    public void notifyAsync(String uri, String ip) {
        HitDto dto = new HitDto();
        dto.setApp(appName);
        dto.setUri(uri);
        dto.setIp(ip);
        dto.setTimestamp(LocalDateTime.now());

        client
                .post()
                .uri("/hit")
                .body(BodyInserters.fromValue(dto))
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(throwable -> log.error("Unable to call hit endpoint: " + throwable))
                .subscribe();
    }
}
