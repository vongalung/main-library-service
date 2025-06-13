package com.test.library.main.repository;

import com.test.library.main.model.ReturnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface ReturnStatusRepo extends JpaRepository<ReturnStatus, UUID> {
    @Query("SELECT rs FROM ReturnStatus rs")
    Stream<ReturnStatus> streamAll();
}
