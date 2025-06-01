package com.test.library.main.repository;

import com.test.library.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID>, UserRepoExtension {
    @Query("SELECT u FROM User u WHERE u.masterPerson.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
}
