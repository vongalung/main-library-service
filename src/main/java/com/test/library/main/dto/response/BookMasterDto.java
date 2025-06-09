package com.test.library.main.dto.response;

import java.util.UUID;

public record BookMasterDto(
    UUID id,
    String title,
    String author,
    String synopsis,
    Integer availableUnits
) { }
