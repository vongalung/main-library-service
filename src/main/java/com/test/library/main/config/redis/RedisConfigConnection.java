package com.test.library.main.config.redis;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RedisConfigConnection {
    @NotBlank
    private String host;
    private Integer port;
}
