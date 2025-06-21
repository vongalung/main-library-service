package com.test.library.main.controller;

import com.test.library.main.dto.request.CheckOutWithUserSessionDto;
import com.test.library.main.dto.request.ReturnCheckOutDto;
import com.test.library.main.dto.response.BookDetailDto;
import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.service.BookCheckOutControllerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/book/checkout")
@RequiredArgsConstructor
@Validated
@Log4j2
public class BookCheckOutController {
    final BookCheckOutControllerService bookCheckOutControllerService;

    @GetMapping("/{bookId}")
    public BookDetailDto findByBookId(@PathVariable @NotNull UUID bookId)
            throws BaseApplicationException {
        log.debug("INCOMING REQUEST to Book.findByBookId : {}", bookId);
        return bookCheckOutControllerService.findById(bookId);
    }

    @PostMapping("/{bookId}")
    public BookDetailDto checkOut(@PathVariable @NotNull UUID bookId,
                                  @RequestBody @NotNull @Valid CheckOutWithUserSessionDto request)
            throws BaseApplicationException {
        log.debug("INCOMING REQUEST to Book.checkOut for bookId={} : {}", bookId, request);
        return bookCheckOutControllerService.markCheckOut(bookId, request);
    }

    @PostMapping("/{bookId}/self")
    public BookDetailDto selfCheckOut(@PathVariable @NotNull UUID bookId,
                                      @RequestParam(required = false) LocalDate expectedReturnDate)
            throws BaseApplicationException {
        log.debug("INCOMING REQUEST to Book.selfCheckOut for bookId={} : expectedReturnDate={}",
                bookId, expectedReturnDate);
        return bookCheckOutControllerService.markSelfCheckOut(bookId, expectedReturnDate);
    }

    @PostMapping("/{bookId}/return")
    public BookDetailDto returnCheckOut(@PathVariable @NotNull UUID bookId,
                                        @RequestBody @NotNull @Valid ReturnCheckOutDto request)
            throws BaseApplicationException {
        log.debug("INCOMING REQUEST to Book.returnCheckOut for bookId={} : {}", bookId, request);
        return bookCheckOutControllerService.markReturn(bookId, request.returnStatusId(), request.remarks());
    }
}
