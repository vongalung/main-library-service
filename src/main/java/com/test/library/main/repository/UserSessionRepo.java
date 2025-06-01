package com.test.library.main.repository;

import com.test.library.main.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserSessionRepo extends JpaRepository<UserSession, UUID> {
}
