package com.test.library.main.model;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.AUTO;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
public class CheckOutHistory {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    @CreationTimestamp
    private ZonedDateTime createdDate;
    @NotNull
    private ZonedDateTime checkOutDate;
    @NotNull
    private ZonedDateTime expectedReturnDate;
    private ZonedDateTime returnDate;
    @Enumerated(STRING)
    private ReturnStatus returnStatus;
    private String remarks;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    @Valid
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @NotNull
    @Valid
    private Book book;
}
