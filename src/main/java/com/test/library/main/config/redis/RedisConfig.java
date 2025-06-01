package com.test.library.main.config.redis;

import static org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import static org.springframework.data.redis.connection.jedis.JedisClientConfiguration.builder;

import com.test.library.main.dto.response.NewUserTempDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import java.time.Duration;
import java.util.UUID;

@Configuration
public class RedisConfig {
    @Bean
    JedisConnectionFactory jedisConnectionFactory(
            RedisConfigProperties props) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        RedisConfigConnection conn = props.getConnections().getFirst();
        String hostName = conn.getHost();
        if (hostName != null && !hostName.isBlank()) {
            config.setHostName(hostName);
        }
        if (conn.getPort() != null) {
            config.setPort(conn.getPort());
        }

        RedisConfigAuth auth = props.getAuth();
        if (auth != null && auth.getPassword() != null) {
            config.setPassword(auth.getPassword());
        }

        JedisClientConfigurationBuilder jedisClientConfiguration = builder();
        if (props.getTimeout() != null) {
            jedisClientConfiguration.connectTimeout(
                    Duration.ofSeconds(props.getTimeout()));
        }

        return new JedisConnectionFactory(config,
                jedisClientConfiguration.build());
    }

    @Bean
    @Primary
    RedisTemplate<UUID, NewUserTempDto> newUserRedisTemplate(JedisConnectionFactory factory) {
        RedisTemplate<UUID, NewUserTempDto> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        template.setKeySerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer<NewUserTempDto> serializer =
                new Jackson2JsonRedisSerializer<>(NewUserTempDto.class);
        template.setValueSerializer(serializer);
        return template;
    }
}
