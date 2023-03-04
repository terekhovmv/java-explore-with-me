package ru.practicum.ewm;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.practicum.stats.client.HitSpy;


@Configuration
public class EwmServiceConfigurer implements WebMvcConfigurer {

    private final HitSpy hitSpy;

    public EwmServiceConfigurer(HitSpy hitSpy) {
        this.hitSpy = hitSpy;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(hitSpy);
    }
}
