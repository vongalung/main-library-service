package com.test.library.main.service;

import static com.test.library.main.common.DateTimeUtils.convertToZonedAtLateMidnight;
import static com.test.library.main.common.DtoRemapper.remapBookDetail;
import static com.test.library.main.common.VerifyingUtils.verifyUnreturned;

import com.test.library.main.dto.request.CheckOutWithUserSessionDto;
import com.test.library.main.dto.response.BookDetailDto;
import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.exception.UserHasUnreturnedCheckOutsException;
import com.test.library.main.exception.UserNotFoundException;
import com.test.library.main.model.Book;
import com.test.library.main.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookCheckOutControllerService {
    final BookService bookService;
    final UserService userService;
    final UserSessionService userSessionService;

    @Transactional
    public BookDetailDto markSelfCheckOut(UUID bookId, LocalDate expectedReturnDate) throws BaseApplicationException {
        User user = userSessionService.findUserFromSession();
        return markCheckOut(bookId, user, expectedReturnDate);
    }

    @Transactional
    public BookDetailDto markCheckOut(UUID bookId, CheckOutWithUserSessionDto request) throws BaseApplicationException {
        userSessionService.verifyUserAsAdmin();
        User checkoutUser = userService.findByIdWithCheckOutsFilteredByUnreturnedStatus(request.userId())
                .orElseThrow(UserNotFoundException::new);
        return markCheckOut(bookId, checkoutUser, request.expectedReturnDate());
    }

    @Transactional
    BookDetailDto markCheckOut(UUID bookId, User user, LocalDate expectedReturnDate) throws BaseApplicationException {
        if (verifyUnreturned(user)) {
            throw new UserHasUnreturnedCheckOutsException();
        }
        Book book = bookService.markCheckOut(bookId, user,
                convertToZonedAtLateMidnight(expectedReturnDate));
        return remapBookDetail(book);
    }

    @Transactional
    public BookDetailDto markReturn(UUID bookId, UUID returnStatusId, String remarks) throws BaseApplicationException {
        Book book = bookService.markReturn(bookId, returnStatusId, remarks);
        return remapBookDetail(book);
    }

    public BookDetailDto findById(UUID bookId) throws BaseApplicationException {
        Book book = bookService.findByIdWithCheckOutsFilteredByUnreturnedStatus(bookId);
        return remapBookDetail(book);
    }
}
