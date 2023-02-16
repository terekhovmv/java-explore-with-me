package ru.practicum.stats.client;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HitSpy implements HandlerInterceptor {

    private final StatsServerClient statsServerClient;

    public HitSpy(StatsServerClient statsServerClient) {
        this.statsServerClient = statsServerClient;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        statsServerClient.hit(request.getRequestURI(), request.getRemoteAddr());
        return true;
    }

}
