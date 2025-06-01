package com.test.library.main.service;

import com.test.library.main.config.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailTemplatingService {
    final TemplateEngine templateEngine;
    final AppConfig appConfig;

    public String createRegistrationVerificationEmail(UUID key) {
        final Context ctx = new Context();
        ctx.setVariable("redirect", createRegistrationVerificationRedirectLink(key));
        return templateEngine.process("html/registrationVerificationTemplate.html", ctx);
    }

    String createRegistrationVerificationRedirectLink(UUID key) {
        String baseUrl = appConfig.getApplicationBaseUrl();
        String path = "/user/management/verify";
        return (new DefaultUriBuilderFactory()).builder()
                .host(baseUrl)
                .path(path)
                .queryParam("verificationKey", key)
                .toUriString();
    }
}
