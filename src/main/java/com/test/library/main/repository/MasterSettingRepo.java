package com.test.library.main.repository;

import com.test.library.main.model.MasterSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MasterSettingRepo extends JpaRepository<MasterSetting, UUID> {
    Optional<MasterSetting> findByKey(String key);
}
