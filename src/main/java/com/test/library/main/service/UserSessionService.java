package com.test.library.main.service;

import static java.time.Duration.ofHours;
import static java.time.Duration.ofSeconds;

import com.test.library.main.config.AppConfig;
import com.test.library.main.config.SessionData;
import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.exception.InvalidUserSessionException;
import com.test.library.main.exception.UserSessionExpiresException;
import com.test.library.main.exception.UserSessionIdRequiredException;
import com.test.library.main.model.User;
import com.test.library.main.model.UserSession;
import com.test.library.main.repository.UserSessionRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSessionService {
    final SessionData sessionData;
    final UserSessionRepo userSessionRepo;
    final AppConfig appConfig;

    Duration getSessionTTL() {
        Long ttlSeconds = appConfig.getUserSessionTtlSeconds();
        if (ttlSeconds != null) {
            return ofSeconds(ttlSeconds);
        }
        return null;
    }

    Duration getKeepSessionAliveTTL() {
        Long keepAliveHours = appConfig.getKeepSessionAliveHours();
        if (keepAliveHours != null) {
            return ofHours(keepAliveHours);
        }
        return null;
    }

    @Transactional
    public UserSession createUserSession(User user) {
        UserSession session = createOrUpdateSession(user);

        Duration sessionTtl = getSessionTTL();
        if (sessionTtl != null) {
            session.setExpiresAt(ZonedDateTime.now().plus(sessionTtl));
        }
        return userSessionRepo.save(session);
    }

    UserSession createOrUpdateSession(User user) {
        UserSession session = user.getSession();
        if (session == null) {
            session = new UserSession();
            session.setUser(user);
            user.setSession(session);
            return session;
        }
        ZonedDateTime sessionKeepAlive = session.getExpiresAt();
        Duration keepAlive = getKeepSessionAliveTTL();
        if (keepAlive != null) {
            sessionKeepAlive = sessionKeepAlive.plus(keepAlive);
        }

        ZonedDateTime now = ZonedDateTime.now();
        if (now.isAfter(sessionKeepAlive)) {
            session = new UserSession();
            session.setUser(user);
            user.setSession(session);
        }
        return session;
    }

    @Transactional
    public void removeUserSession() {
        UUID userSession = sessionData.getUserSession();
        if (userSession == null) {
            throw new UserSessionIdRequiredException();
        }
        UserSession session = userSessionRepo.findById(userSession).orElse(null);
        if (session == null) {
            return;
        }
        userSessionRepo.delete(session);
    }

    public User findUserFromSession() throws BaseApplicationException {
        UUID userSession = sessionData.getUserSession();
        if (userSession == null) {
            throw new UserSessionIdRequiredException();
        }
        UserSession session = userSessionRepo.findById(userSession)
                .orElseThrow(InvalidUserSessionException::new);

        ZonedDateTime expiration = session.getExpiresAt();
        if (expiration != null && ZonedDateTime.now().isAfter(expiration)) {
            throw new UserSessionExpiresException();
        }
        return session.getUser();
    }
}
