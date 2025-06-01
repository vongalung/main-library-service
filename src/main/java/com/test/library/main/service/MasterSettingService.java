package com.test.library.main.service;

import com.test.library.main.model.MasterSetting;
import com.test.library.main.repository.MasterSettingRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class MasterSettingService {
    final MasterSettingRepo masterSettingRepo;

    Optional<String> getSettingValues(String key) {
        return masterSettingRepo.findByKey(key).map(MasterSetting::getValue);
    }

    public <R> Optional<R> getSettingValues(String key, Function<String, R> transformer) {
        return getSettingValues(key).map(transformer);
    }

    public static class SettingKey {
        public static final String DEFAULT_RETURN_DAYS = "checkout.return.days";
    }
}
