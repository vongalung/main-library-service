package com.test.library.main.model;

public interface UserBase {
    java.util.UUID getId();

    java.time.ZonedDateTime getCreatedDate();

    Boolean getIsActive();

    MasterPerson getMasterPerson();
}
