package com.test.library.main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class MasterBookWithCountedUnits {
    private UUID id;
    private ZonedDateTime createdDate;
    private String title;
    private String author;
    private String synopsis;
    private Long availableUnits;
}
