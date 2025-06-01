package com.test.library.main.common;

import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.exception.UnauthorizedAccessException;
import com.test.library.main.model.Book;
import com.test.library.main.model.User;
import com.test.library.main.model.UserRole;
import java.util.List;

public class VerifyingUtils {
    static final List<UserRole> adminRoles = List.of(UserRole.ADMIN);

    public static boolean verifyUnreturned(Book book) {
        return book.getCheckOutHistories().stream()
                .anyMatch(h -> h.getReturnDate() == null);
    }

    public static boolean verifyUnreturned(User user) {
        return user.getCheckOutHistories().stream()
                .anyMatch(h -> h.getReturnDate() == null);
    }

    public static void verifyAsAdmin(User user) throws BaseApplicationException {
        if (user != null && adminRoles.contains(user.getUserRole())) {
            return;
        }
        throw new UnauthorizedAccessException();
    }
}
