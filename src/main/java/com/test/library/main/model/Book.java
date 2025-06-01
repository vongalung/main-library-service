package com.test.library.main.model;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.AUTO;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@FilterDef(name = "Book_unreturnedCheckOuts")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    @CreationTimestamp
    private ZonedDateTime createdDate;
    @NotNull
    private Boolean isAvailable = Boolean.TRUE;

    @ManyToOne
    @JoinColumn(name = "master_book_id")
    @NotNull
    @Valid
    private MasterBook masterBook;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "book", orphanRemoval = true)
    @Filter(name = "Book_unreturnedCheckOuts", condition = "return_date IS NULL")
    @NotNull
    private List<CheckOutHistory> checkOutHistories = new ArrayList<>();
}
