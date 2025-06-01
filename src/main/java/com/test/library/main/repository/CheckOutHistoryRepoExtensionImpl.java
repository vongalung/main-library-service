package com.test.library.main.repository;

import com.test.library.main.model.Book;
import com.test.library.main.model.CheckOutHistory;
import com.test.library.main.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CheckOutHistoryRepoExtensionImpl implements CheckOutHistoryRepoExtension {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public CheckOutHistory safeInsert(CheckOutHistory history, Book book, User user) {
        Book safeBook = entityManager.getReference(Book.class, book.getId());
        User safeUser = entityManager.getReference(User.class, user.getId());

        history.setBook(safeBook);
        history.setUser(safeUser);
        safeBook.getCheckOutHistories().add(history);
        safeUser.getCheckOutHistories().add(history);
        entityManager.persist(history);
        return history;
    }
}
