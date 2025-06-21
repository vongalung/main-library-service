package com.test.library.main.service;

import com.test.library.main.model.ReturnStatus;
import com.test.library.main.repository.ReturnStatusRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReturnStatusService {
    final ReturnStatusRepo returnStatusRepo;

    @Transactional
    public Stream<ReturnStatus> findAll() {
        return returnStatusRepo.streamAll();
    }

    public Optional<ReturnStatus> findById(UUID returnStatusId) {
        return returnStatusRepo.findById(returnStatusId);
    }
}
