package com.test.library.main.model;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
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
@FilterDef(name = "User_unreturnedCheckOuts")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    @CreationTimestamp
    private ZonedDateTime createdDate;
    @NotNull
    private Boolean isActive = true;

    @Enumerated(STRING)
    @NotNull
    private UserRole userRole = UserRole.USER;
    @NotNull
    private byte[] encryptedPassword;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "master_person_id")
    @NotNull
    @Valid
    private MasterPerson masterPerson;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "user", orphanRemoval = true)
    @Filter(name = "User_unreturnedCheckOuts", condition = "return_date IS NULL")
    @NotNull
    private List<CheckOutHistory> checkOutHistories = new ArrayList<>();

    @OneToOne(cascade = ALL, mappedBy = "user", orphanRemoval = true)
    private UserSession session;
}
