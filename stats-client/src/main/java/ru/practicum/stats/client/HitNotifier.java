package ru.practicum.stats.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.stats.dto.RegisterHitDto;

import java.time.LocalDateTime;

@Slf4j
@Component
public class HitNotifier {

    private final WebClient client;
    private final String appName;

    public HitNotifier(StatsClientConfiguration configuration, WebClientFactory webClientFactory) {
        this.client = webClientFactory.getWebClient();
        this.appName = configuration.getAppName();
    }

    public void notifyAsync(String uri, String ip) {
        RegisterHitDto dto = new RegisterHitDto();
        dto.setApp(appName);
        dto.setUri(uri);
        dto.setIp(ip);
        dto.setTimestamp(LocalDateTime.now());

        client.post()
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
