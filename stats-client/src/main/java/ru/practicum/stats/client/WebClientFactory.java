package ru.practicum.stats.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientFactory {
    private final WebClient webClient;

    public WebClientFactory(StatsClientConfiguration configuration, ObjectMapper mapper) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs()
                            .jackson2JsonEncoder(new Jackson2JsonEncoder(mapper, MediaType.APPLICATION_JSON));
                    configurer.defaultCodecs()
                            .jackson2JsonDecoder(new Jackson2JsonDecoder(mapper, MediaType.APPLICATION_JSON));
                })
                .build();

        this.webClient = WebClient.builder()
                .baseUrl(configuration.getServiceUrl())
                .exchangeStrategies(strategies)
                .build();
    }

    public WebClient getWebClient() {
        return webClient;
    }
}
