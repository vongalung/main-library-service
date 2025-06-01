package com.test.library.main.service;

import static com.test.library.main.common.DateTimeUtils.adjustToLateMidnight;
import static com.test.library.main.common.VerifyingUtils.verifyUnreturned;

import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.exception.BookIsCurrentlyCheckedOutException;
import com.test.library.main.exception.BookNotCheckedOutException;
import com.test.library.main.model.Book;
import com.test.library.main.model.CheckOutHistory;
import com.test.library.main.model.ReturnStatus;
import com.test.library.main.model.User;
import com.test.library.main.repository.CheckOutHistoryRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckOutHistoryService {
    final CheckOutHistoryRepo checkOutHistoryRepo;

    final MasterSettingService masterSettingService;

    @Value("${app.checkout.default-return-days}")
    private Long defaultReturnDays;

    @Transactional
    public Book markCheckOut(Book book, User user, ZonedDateTime expectedReturnDate)
            throws BaseApplicationException {
        if (verifyUnreturned(book)) {
            throw new BookIsCurrentlyCheckedOutException();
        }

        Long returnDays = masterSettingService.getSettingValues(
                MasterSettingService.SettingKey.DEFAULT_RETURN_DAYS,
                Long::parseLong)
                .orElse(defaultReturnDays);
        ZonedDateTime checkOutDate = ZonedDateTime.now();
        if (expectedReturnDate == null) {
            expectedReturnDate = adjustToLateMidnight(checkOutDate.plusDays(returnDays));
        }

        CheckOutHistory checkOutHistory = new CheckOutHistory();
        checkOutHistory.setCheckOutDate(checkOutDate);
        checkOutHistory.setExpectedReturnDate(expectedReturnDate);
        checkOutHistory = checkOutHistoryRepo.safeInsert(checkOutHistory, book, user);
        return checkOutHistory.getBook();
    }

    @Transactional
    void markReturn(Book book, ReturnStatus returnStatus, String remarks)
            throws BaseApplicationException {
        if (!verifyUnreturned(book)) {
            throw new BookNotCheckedOutException();
        }
        List<CheckOutHistory> toBeSaved = book.getCheckOutHistories().stream()
                .map(h -> markReturn(h, returnStatus, remarks))
                .toList();
        checkOutHistoryRepo.saveAllAndFlush(toBeSaved);
    }

    CheckOutHistory markReturn(CheckOutHistory history, ReturnStatus returnStatus, String remarks) {
        history.setReturnStatus(returnStatus);
        history.setRemarks(remarks);
        history.setReturnDate(ZonedDateTime.now());
        return history;
    }
}
