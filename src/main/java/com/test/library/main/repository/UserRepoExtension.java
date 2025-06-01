package com.test.library.main.repository;

import com.test.library.main.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepoExtension {
    Optional<User> findByIdWithCheckOutsFilteredByUnreturnedStatus(UUID userId);
}
