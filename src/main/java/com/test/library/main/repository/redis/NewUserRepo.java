package com.test.library.main.repository.redis;

import static jakarta.transaction.Transactional.TxType.NOT_SUPPORTED;
import static java.time.Duration.ofSeconds;
import static java.util.UUID.randomUUID;

import com.test.library.main.config.AppConfig;
import com.test.library.main.dto.response.NewUserTempDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class NewUserRepo {
    private final RedisTemplate<UUID, NewUserTempDto> newUserRedis;
    private final AppConfig appConfig;

    UUID generateKey() {
        return randomUUID();
    }

    Duration getTTL() {
        Long ttlSeconds = appConfig.getNewUserTtlSeconds();
        if (ttlSeconds != null) {
            return ofSeconds(ttlSeconds);
        }
        return null;
    }

    @Transactional(NOT_SUPPORTED)
    public UUID saveTempNewUser(NewUserTempDto newUser) {
        if (newUser == null) {
            return null;
        }
        UUID key = generateKey();
        log.debug("cache new user {} with {}", newUser, key);
        newUserRedis.opsForValue().set(key, newUser);
        Duration ttl = getTTL();
        if (ttl != null) {
            newUserRedis.expire(key, ttl);
        }
        return key;
    }

    @Transactional(NOT_SUPPORTED)
    public NewUserTempDto findTempNewUser(UUID key) {
        if (key == null) {
            return null;
        }
        NewUserTempDto newUser = newUserRedis.opsForValue().get(key);
        log.info("read new user cache {} with {}", newUser, key);
        newUserRedis.delete(key);
        return newUser;
    }
}
