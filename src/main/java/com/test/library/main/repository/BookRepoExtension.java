package com.test.library.main.repository;

import com.test.library.main.common.PaginationFactory;
import com.test.library.main.model.Book;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface BookRepoExtension {
    Optional<Book> findByIdWithCheckOutsFilteredByUnreturnedStatus(UUID bookId);
    PaginationFactory<Book> findByMasterId(UUID masterId, Pageable pagination);
}
