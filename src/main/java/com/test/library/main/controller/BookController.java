package com.test.library.main.controller;

import com.test.library.main.dto.request.BookSearchRequestDto;
import com.test.library.main.dto.request.CheckOutWithUserSessionDto;
import com.test.library.main.dto.request.ReturnCheckOutDto;
import com.test.library.main.dto.response.BookDto;
import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.service.BookControllerService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@Validated
@Log4j2
public class BookController {
    final BookControllerService bookControllerService;

    @GetMapping({"/", ""})
    @Transactional
    public Page<BookDto> findAll(BookSearchRequestDto request) {
        log.debug("INCOMING REQUEST to Book.findAll : {}", request);
        return bookControllerService.findAll(request);
    }

    @GetMapping("/{bookId}")
    public BookDto findByBookId(@PathVariable @NotNull UUID bookId)
            throws BaseApplicationException {
        log.debug("INCOMING REQUEST to Book.findByBookId : {}", bookId);
        return bookControllerService.findById(bookId);
    }

    @PostMapping("/{bookId}/checkout")
    public BookDto checkOut(@PathVariable @NotNull UUID bookId,
                            @RequestBody @NotNull @Valid CheckOutWithUserSessionDto request)
            throws BaseApplicationException {
        log.debug("INCOMING REQUEST to Book.checkOut for bookId={} : {}", bookId, request);
        return bookControllerService.markCheckOut(bookId, request);
    }

    @PostMapping("/{bookId}/checkout/self")
    public BookDto selfCheckOut(@PathVariable @NotNull UUID bookId,
                                @RequestParam(required = false) LocalDate expectedReturnDate)
            throws BaseApplicationException {
        log.debug("INCOMING REQUEST to Book.selfCheckOut for bookId={} : expectedReturnDate={}",
                bookId, expectedReturnDate);
        return bookControllerService.markSelfCheckOut(bookId, expectedReturnDate);
    }

    @PostMapping("/{bookId}/return")
    public BookDto returnCheckOut(@PathVariable @NotNull UUID bookId,
                                  @RequestBody @NotNull @Valid ReturnCheckOutDto request)
            throws BaseApplicationException {
        log.debug("INCOMING REQUEST to Book.returnCheckOut for bookId={} : {}", bookId, request);
        return bookControllerService.markReturn(bookId, request.returnStatus(), request.remarks());
    }
}
