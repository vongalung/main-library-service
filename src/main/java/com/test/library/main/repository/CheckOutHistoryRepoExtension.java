package com.test.library.main.repository;

import com.test.library.main.model.Book;
import com.test.library.main.model.CheckOutHistory;
import com.test.library.main.model.User;

public interface CheckOutHistoryRepoExtension {
    CheckOutHistory safeInsert(CheckOutHistory history, Book book, User user);
}
