package com.test.library.main.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import java.util.UUID;

@Component
@RequestScope
@Getter
@ToString
public class SessionData {
    private final UUID userSession;

    SessionData(HttpServletRequest request) {
        String rawSessionId = request.getHeader("sessionId");
        if (rawSessionId == null || rawSessionId.isBlank()) {
            userSession = null;
            return;
        }
        userSession = UUID.fromString(rawSessionId);
    }
}
