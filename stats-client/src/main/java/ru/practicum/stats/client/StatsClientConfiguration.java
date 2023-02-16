package ru.practicum.stats.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "stats-client")
@Getter
@Setter
public class StatsClientConfiguration {
    String serverUrl;
    String appName;
}
