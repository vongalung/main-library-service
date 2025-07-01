package com.test.library.main.config;

import static org.slf4j.MDC.clear;
import static org.slf4j.MDC.put;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {
    final SessionData sessionData;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean preHandleResult = HandlerInterceptor.super.preHandle(request, response, handler);
        put("requestId", UUID.randomUUID().toString());
        put("sessionId", getUserSessionId());
        return preHandleResult;
    }

    String getUserSessionId() {
        UUID sessionId = sessionData.getUserSession();
        if (sessionId == null) {
            return null;
        }
        return sessionId.toString();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        clear();
    }
}
