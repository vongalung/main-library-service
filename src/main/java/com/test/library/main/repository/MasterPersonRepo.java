package com.test.library.main.repository;

import com.test.library.main.model.MasterPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface MasterPersonRepo extends JpaRepository<MasterPerson, UUID> {
}
