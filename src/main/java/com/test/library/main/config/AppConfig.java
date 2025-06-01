package com.test.library.main.config;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppConfig {
    @NotNull
    @PositiveOrZero
    private Long userSessionTtlSeconds;
    @NotNull
    @PositiveOrZero
    private Long newUserTtlSeconds;
    @NotNull
    @PositiveOrZero
    private Long keepSessionAliveHours;
}
