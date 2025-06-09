package com.test.library.main.service;

import com.test.library.main.common.PaginationFactory;
import com.test.library.main.dto.request.BookSearchRequestDto;
import com.test.library.main.dto.request.NewBookDto;
import com.test.library.main.model.MasterBook;
import com.test.library.main.repository.MasterBookRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class MasterBookService {
    final MasterBookRepo masterBookRepo;

    public Optional<MasterBook> findById(UUID masterId) {
        return masterBookRepo.findById(masterId);
    }

    public Page<String> findUniqueTitles(String title, Integer page, Integer limit) {
        Pageable pagination = null;
        if (page != null && limit != null) {
            pagination = PageRequest.of(page, limit);
        }
        return masterBookRepo.findUniqueTitles(title, pagination);
    }

    public Page<String> findUniqueAuthors(String author, Integer page, Integer limit) {
        Pageable pagination = null;
        if (page != null && limit != null) {
            pagination = PageRequest.of(page, limit);
        }
        return masterBookRepo.findUniqueAuthors(author, pagination);
    }

    @Transactional
    public PaginationFactory<MasterBook> findAll(BookSearchRequestDto request) {
        return masterBookRepo.findAllWithParameters(request);
    }

    public Optional<MasterBook> findByTitleAndAuthor(String title, String author) {
        return masterBookRepo.findByTitleAndAuthor(title,author);
    }

    @Transactional
    public MasterBook save(NewBookDto newBook) {
        MasterBook matchingBook = findByTitleAndAuthor(newBook.title(), newBook.author())
                .orElse(null);
        if (matchingBook != null) {
            log.info("Book with same \"author\" and \"title\" was found. Not adding new book: {}",
                    newBook);
            return matchingBook;
        }

        matchingBook = new MasterBook();
        matchingBook.setTitle(newBook.title());
        matchingBook.setAuthor(newBook.author());
        matchingBook.setSynopsis(newBook.synopsis());
        return masterBookRepo.save(matchingBook);
    }
}
