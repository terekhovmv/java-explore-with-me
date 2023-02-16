package ru.practicum.stats.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Slf4j
@Service
public class StatsServerClient {

    protected final RestTemplate rest;

    public StatsServerClient(
            @Value("${stats-server.url}") String serverUrl,
            @Value("${stats-server.app-name}") String appName,
            RestTemplateBuilder builder
    ) {
        rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void hit(String uri, String ip) {
        log.debug("Call {} from {}", uri, ip);
    }
}
