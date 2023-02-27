package ru.practicum.stats.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.stats.dto.DtoFormat;
import ru.practicum.stats.dto.SummaryDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StatsProvider {
    private final StatsClientConfiguration configuration;
    private final WebClient client;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DtoFormat.DATE_TIME_FORMAT);

    public StatsProvider(StatsClientConfiguration configuration, WebClientFactory webClientFactory) {
        this.configuration = configuration;
        this.client = webClientFactory.getWebClient();
    }

    public Map<String, Long> getStats(String appName, Collection<String> uris, LocalDateTime start, LocalDateTime end, boolean unique) {
        StringBuilder request = new StringBuilder("/stats?");
        request.append("?uris=");
        request.append(String.join(",", uris));
        if (start != null) {
            request.append("&start=");
            request.append(dateTimeFormatter.format(start));
        }
        if (end != null) {
            request.append("&end=");
            request.append(dateTimeFormatter.format(end));
        }
        request.append("&unique=");
        request.append(unique);

        List<SummaryDto> result = client
                .get()
                .uri(request.toString())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<SummaryDto>>() {
                })
                .block();

        if (result == null) {
            return Map.of();
        }

        return result
                .stream()
                .filter(item -> item.getApp().equals(appName))
                .collect(Collectors.toMap(SummaryDto::getUri, SummaryDto::getHits));
    }

    public Map<String, Long> getCurrentAppStats(Collection<String> uris, LocalDateTime start, LocalDateTime end, boolean unique) {
        return getStats(configuration.getAppName(), uris, start, end, unique);
    }
}
