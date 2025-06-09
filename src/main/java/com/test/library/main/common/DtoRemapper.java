package com.test.library.main.common;

import com.test.library.main.dto.response.*;
import com.test.library.main.model.*;
import java.time.ZonedDateTime;
import java.util.List;

public class DtoRemapper {
    public static ReturnStatusDto remapReturnStatus(ReturnStatus status) {
        if (status == null) {
            return null;
        }
        return new ReturnStatusDto(
                status.getId(),
                status.getStatus(),
                status.getDescription());
    }

    public static BookDetailDto remapBookDetail(Book book) {
        if (book == null) {
            return null;
        }
        CheckOutHistory history = null;
        List<CheckOutHistory> histories = book.getCheckOutHistories();
        if (histories != null && !histories.isEmpty()) {
            history = histories.getFirst();
        }

        MasterBook master = book.getMasterBook();
        return new BookDetailDto(
                book.getId(),
                master.getTitle(),
                master.getAuthor(),
                master.getSynopsis(),
                book.getIsAvailable(),
                history == null ? null : history.getCheckOutDate(),
                history == null ? null : history.getExpectedReturnDate(),
                history == null ? null : remapCheckOutUser(history.getUser()));
    }

    public static BookMasterDto remapBookMaster(MasterBook book) {
        if (book == null) {
            return null;
        }
        return new BookMasterDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getSynopsis(),
                book.getBooks().size());
    }

    public static BookUnitDto remapBookUnit(Book book) {
        if (book == null) {
            return null;
        }
        CheckOutHistory history = null;
        List<CheckOutHistory> histories = book.getCheckOutHistories();
        if (histories != null && !histories.isEmpty()) {
            history = histories.getFirst();
        }
        return new BookUnitDto(
                book.getId(),
                book.getIsAvailable(),
                history == null ? null : history.getCheckOutDate(),
                history == null ? null : history.getExpectedReturnDate(),
                history == null ? null : remapCheckOutUser(history.getUser()));
    }

    public static CheckOutUserDto remapCheckOutUser(User user) {
        if (user == null) {
            return null;
        }
        MasterPerson details = user.getMasterPerson();
        return new CheckOutUserDto(
                details.getEmail(),
                details.getFullName());
    }

    public static UserDto remapUser(User user) {
        if (user == null) {
            return null;
        }

        CheckOutBookDto checkOutBook = null;
        List<CheckOutHistory> histories = user.getCheckOutHistories();
        if (histories != null && !histories.isEmpty()) {
            CheckOutHistory history = histories.getFirst();
            MasterBook bookDetails = history.getBook().getMasterBook();

            checkOutBook = new CheckOutBookDto(
                    bookDetails.getTitle(),
                    bookDetails.getAuthor(),
                    history.getCheckOutDate(),
                    history.getExpectedReturnDate());
        }

        MasterPerson userDetails = user.getMasterPerson();
        return new UserDto(
                userDetails.getEmail(),
                userDetails.getFullName(),
                verifyLoggedIn(user.getSession()),
                checkOutBook);
    }

    static boolean verifyLoggedIn(UserSession session) {
        if (session == null) {
            return false;
        }
        return ZonedDateTime.now().isAfter(session.getExpiresAt());
    }
}
