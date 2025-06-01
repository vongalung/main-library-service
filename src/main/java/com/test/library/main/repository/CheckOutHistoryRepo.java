package com.test.library.main.repository;

import com.test.library.main.model.CheckOutHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CheckOutHistoryRepo extends JpaRepository<CheckOutHistory, UUID>,
        CheckOutHistoryRepoExtension {
}
