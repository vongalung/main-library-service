package com.test.library.main.common;

import com.test.library.main.dto.response.BookDto;
import com.test.library.main.dto.response.UserDto;
import com.test.library.main.model.*;
import java.util.List;

public class DtoRemapper {
    public static BookDto remapBook(Book book) {
        if (book == null) {
            return null;
        }
        MasterBook master = book.getMasterBook();
        CheckOutHistory history = null;
        List<CheckOutHistory> histories = book.getCheckOutHistories();
        if (histories != null && !histories.isEmpty()) {
            history = histories.getFirst();
        }
        return new BookDto(
                book.getId(),
                master.getTitle(),
                master.getAuthor(),
                master.getSynopsis(),
                book.getIsAvailable(),
                history == null ? null : history.getCheckOutDate(),
                history == null ? null : history.getExpectedReturnDate(),
                history == null ? null : remapUser(history.getUser()));
    }

    public static UserDto remapUser(User user) {
        if (user == null) {
            return null;
        }
        MasterPerson details = user.getMasterPerson();
        return new UserDto(
                details.getEmail(),
                details.getFullName());
    }
}
