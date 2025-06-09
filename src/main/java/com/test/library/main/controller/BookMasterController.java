package com.test.library.main.controller;

import com.test.library.main.dto.request.BookSearchRequestDto;
import com.test.library.main.dto.request.NewBookDto;
import com.test.library.main.dto.response.BookDetailDto;
import com.test.library.main.dto.response.BookMasterDto;
import com.test.library.main.dto.response.BookUnitDto;
import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.service.BookMasterControllerService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@Validated
@Log4j2
public class BookMasterController {
    final BookMasterControllerService bookMasterControllerService;

    @GetMapping({"/", ""})
    @Transactional
    public Page<BookMasterDto> findAll(BookSearchRequestDto request) {
        log.debug("INCOMING REQUEST to BookMaster.findAll : {}", request);
        return bookMasterControllerService.findAll(request);
    }

    @GetMapping("/{masterId}")
    @Transactional
    public Page<BookUnitDto> findUnitsByMasterId(@PathVariable @NotNull UUID masterId,
                                                 @RequestParam(required = false) @PositiveOrZero Integer page,
                                                 @RequestParam(required = false) @Positive Integer pagesize) {
        log.debug("INCOMING REQUEST to BookMaster.findUnitsByMasterId : {}", masterId);
        return bookMasterControllerService.findUnitsByMasterId(masterId, page, pagesize);
    }

    @PostMapping({"/", ""})
    @Transactional
    public BookMasterDto addNewMasterBook(@RequestBody @NotNull @Valid NewBookDto newBook)
            throws BaseApplicationException {
        log.debug("INCOMING REQUEST to BookMaster.addNewMasterBook : {}", newBook);
        return bookMasterControllerService.addNewMasterBook(newBook);
    }

    @PutMapping("/{masterId}")
    @Transactional
    public BookDetailDto addNewBookUnit(@PathVariable @NotNull UUID masterId,
                                        @RequestParam(required = false, defaultValue = "true")
                                        Boolean available)
            throws BaseApplicationException {
        log.debug("INCOMING REQUEST to BookMaster.addNewBookUnit : {}", masterId);
        return bookMasterControllerService.addNewBookUnit(masterId, available);
    }
}
