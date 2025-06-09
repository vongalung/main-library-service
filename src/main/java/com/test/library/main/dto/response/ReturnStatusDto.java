package com.test.library.main.dto.response;

import java.util.UUID;

public record ReturnStatusDto(
        UUID id,
        String status,
        String description
) {}
