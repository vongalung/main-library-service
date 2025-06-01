package com.test.library.main.config.redis;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.redis")
@Getter
@Setter
public class RedisConfigProperties {
    @NotEmpty
    private List<@Valid @NonNull RedisConfigConnection> connections;
    private Integer database;
    @PositiveOrZero
    private Long timeout;
    @Valid
    private RedisConfigAuth auth;
}
