package com.test.library.main.model;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.AUTO;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "book_title", columnNames = {"title", "author"})})
@Data
public class MasterBook {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    @CreationTimestamp
    private ZonedDateTime createdDate;
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    private String synopsis;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "masterBook", orphanRemoval = true)
    @NotNull
    private List<Book> books = new ArrayList<>();
}
