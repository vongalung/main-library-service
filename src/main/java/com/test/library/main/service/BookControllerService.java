package com.test.library.main.service;

import static com.test.library.main.common.DateTimeUtils.convertToZonedAtLateMidnight;
import static com.test.library.main.common.DtoRemapper.remapBook;
import static com.test.library.main.common.VerifyingUtils.verifyAsAdmin;
import static com.test.library.main.common.VerifyingUtils.verifyUnreturned;

import com.test.library.main.common.DtoRemapper;
import com.test.library.main.common.PaginationFactory;
import com.test.library.main.dto.request.BookSearchRequestDto;
import com.test.library.main.dto.request.CheckOutWithUserSessionDto;
import com.test.library.main.dto.response.BookDto;
import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.exception.UserHasUnreturnedCheckOutsException;
import com.test.library.main.exception.UserNotFoundException;
import com.test.library.main.model.Book;
import com.test.library.main.model.ReturnStatus;
import com.test.library.main.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookControllerService {
    final BookService bookService;
    final UserService userService;
    final UserSessionService userSessionService;

    @Transactional
    public BookDto markSelfCheckOut(UUID bookId, LocalDate expectedReturnDate) throws BaseApplicationException {
        User user = userSessionService.findPersonByUserSession();
        return markCheckOut(bookId, user, expectedReturnDate);
    }

    @Transactional
    public BookDto markCheckOut(UUID bookId, CheckOutWithUserSessionDto request) throws BaseApplicationException {
        User user = userSessionService.findPersonByUserSession();
        verifyAsAdmin(user);
        User checkoutUser = userService.findByIdWithCheckOutsFilteredByUnreturnedStatus(request.userId())
                .orElseThrow(UserNotFoundException::new);
        return markCheckOut(bookId, checkoutUser, request.expectedReturnDate());
    }

    @Transactional
    BookDto markCheckOut(UUID bookId, User user, LocalDate expectedReturnDate) throws BaseApplicationException {
        if (verifyUnreturned(user)) {
            throw new UserHasUnreturnedCheckOutsException();
        }
        Book book = bookService.markCheckOut(bookId, user,
                convertToZonedAtLateMidnight(expectedReturnDate));
        return remapBook(book);
    }

    @Transactional
    public BookDto markReturn(UUID bookId, ReturnStatus returnStatus, String remarks) throws BaseApplicationException {
        Book book = bookService.markReturn(bookId, returnStatus, remarks);
        return remapBook(book);
    }

    public BookDto findById(UUID bookId) throws BaseApplicationException {
        Book book = bookService.findByIdWithCheckOutsFilteredByUnreturnedStatus(bookId);
        return remapBook(book);
    }

    @Transactional
    public Page<BookDto> findAll(BookSearchRequestDto request) {
        PaginationFactory<Book> paginationFactory = bookService.findAll(request);
        try (Stream<Book> stream = paginationFactory.stream()) {
            return paginationFactory.replacePagination(stream.map(DtoRemapper::remapBook))
                    .finalizePage();
        }
    }
}
