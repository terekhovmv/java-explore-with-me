package ru.practicum.stats.client;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HitSpy implements HandlerInterceptor {

    private final HitNotifier hitNotifier;

    public HitSpy(HitNotifier hitNotifier) {
        this.hitNotifier = hitNotifier;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        hitNotifier.notifyAsync(request.getRequestURI(), request.getRemoteAddr());
        return true;
    }

}
