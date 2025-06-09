package com.test.library.main.model;

import static jakarta.persistence.GenerationType.AUTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
public class ReturnStatus {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    @CreationTimestamp
    private ZonedDateTime createdDate;
    @Column(unique = true)
    @NotBlank
    private String status;
    private String description;
}
