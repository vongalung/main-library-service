package com.test.library.main.service;

import com.test.library.main.common.PaginationFactory;
import com.test.library.main.dto.request.BookSearchRequestDto;
import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.exception.BookNotFoundException;
import com.test.library.main.model.Book;
import com.test.library.main.model.ReturnStatus;
import com.test.library.main.model.User;
import com.test.library.main.repository.BookRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {
    final BookRepo bookRepo;
    final CheckOutHistoryService checkOutHistoryService;

    public Book findByIdWithCheckOutsFilteredByUnreturnedStatus(UUID bookId) throws BaseApplicationException {
        return bookRepo.findByIdWithCheckOutsFilteredByUnreturnedStatus(bookId)
                .orElseThrow(BookNotFoundException::new);
    }

    @Transactional
    public Book markCheckOut(UUID bookId, User user, ZonedDateTime expectedReturnDate)
            throws BaseApplicationException {
        Book book = bookRepo.findByIdWithCheckOutsFilteredByUnreturnedStatus(bookId)
                .orElseThrow(BookNotFoundException::new);
        return checkOutHistoryService.markCheckOut(book, user, expectedReturnDate);
    }

    @Transactional
    public Book markReturn(UUID bookId, ReturnStatus returnStatus, String remarks) throws BaseApplicationException {
        Book book = bookRepo.findByIdWithCheckOutsFilteredByUnreturnedStatus(bookId)
                .orElseThrow(BookNotFoundException::new);
        checkOutHistoryService.markReturn(book, returnStatus, remarks);
        return bookRepo.findByIdWithCheckOutsFilteredByUnreturnedStatus(bookId)
                .orElseThrow(BookNotFoundException::new);
    }

    @Transactional
    public PaginationFactory<Book> findAll(BookSearchRequestDto request) {
        return bookRepo.findAllWithCheckOutsFilteredByUnreturnedStatus(request);
    }
}
