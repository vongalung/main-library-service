package com.test.library.main.service;

import com.test.library.main.common.PaginationFactory;
import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.exception.BookNotFoundException;
import com.test.library.main.exception.InvalidReturnStatusException;
import com.test.library.main.model.Book;
import com.test.library.main.model.MasterBook;
import com.test.library.main.model.ReturnStatus;
import com.test.library.main.model.User;
import com.test.library.main.repository.BookRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {
    final BookRepo bookRepo;
    final ReturnStatusService returnStatusService;
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
    public Book markReturn(UUID bookId, UUID returnStatusId, String remarks) throws BaseApplicationException {
        Book book = bookRepo.findByIdWithCheckOutsFilteredByUnreturnedStatus(bookId)
                .orElseThrow(BookNotFoundException::new);
        ReturnStatus status = returnStatusService.findById(returnStatusId)
                .orElseThrow(InvalidReturnStatusException::new);
        checkOutHistoryService.markReturn(book, status, remarks);
        return bookRepo.findByIdWithCheckOutsFilteredByUnreturnedStatus(bookId)
                .orElseThrow(BookNotFoundException::new);
    }

    @Transactional
    public PaginationFactory<Book> findByMasterId(UUID masterId, Integer page, Integer limit) {
        Pageable pagination = null;
        if (page != null && limit != null) {
            pagination = PageRequest.of(page, limit);
        }
        return bookRepo.findByMasterId(masterId, pagination);
    }

    @Transactional
    public Book addNewBook(MasterBook masterBook, Boolean isAvailable) {
        Book book = new Book();
        book.setIsAvailable(isAvailable);
        book.setMasterBook(masterBook);
        masterBook.getBooks().add(book);
        return bookRepo.save(book);
    }
}
