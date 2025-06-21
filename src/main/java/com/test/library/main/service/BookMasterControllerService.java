package com.test.library.main.service;

import com.test.library.main.common.DtoRemapper;
import com.test.library.main.common.PaginationFactory;
import com.test.library.main.dto.request.BookSearchRequestDto;
import com.test.library.main.dto.request.NewBookDto;
import com.test.library.main.dto.response.BookDetailDto;
import com.test.library.main.dto.response.BookMasterDto;
import com.test.library.main.dto.response.BookUnitDto;
import com.test.library.main.exception.BaseApplicationException;
import com.test.library.main.exception.TitleNotFoundException;
import com.test.library.main.model.Book;
import com.test.library.main.model.MasterBook;
import com.test.library.main.model.MasterBookWithCountedUnits;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookMasterControllerService {
    final MasterBookService masterBookService;
    final BookService bookService;
    final UserSessionService userSessionService;

    @Transactional
    public Page<BookMasterDto> findAll(BookSearchRequestDto request) {
        PaginationFactory<MasterBookWithCountedUnits> paginationFactory = masterBookService.findAll(request);
        try (Stream<MasterBookWithCountedUnits> stream = paginationFactory.stream()) {
            return paginationFactory.replacePagination(stream.map(DtoRemapper::remapBookMaster))
                    .finalizePage();
        }
    }

    @Transactional
    public Page<BookUnitDto> findUnitsByMasterId(UUID masterId, Integer page, Integer pagesize) {
        PaginationFactory<Book> paginationFactory = bookService.findByMasterId(masterId, page, pagesize);
        try (Stream<Book> stream = paginationFactory.stream()) {
            return paginationFactory.replacePagination(stream.map(DtoRemapper::remapBookUnit))
                    .finalizePage();
        }
    }

    @Transactional
    public BookMasterDto addNewMasterBook(NewBookDto newBook) throws BaseApplicationException{
        userSessionService.verifyUserAsAdmin();
        MasterBook masterBook = masterBookService.save(newBook);
        return DtoRemapper.remapBookMaster(masterBook);
    }

    @Transactional
    public BookDetailDto addNewBookUnit(UUID masterId, Boolean isAvailable)
            throws BaseApplicationException {
        userSessionService.verifyUserAsAdmin();
        MasterBook masterBook = masterBookService.findById(masterId)
                .orElseThrow(TitleNotFoundException::new);
        Book newBookUnit = bookService.addNewBook(masterBook, isAvailable);
        return DtoRemapper.remapBookDetail(newBookUnit);
    }
}
