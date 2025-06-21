package com.test.library.main.repository;

import com.test.library.main.common.PaginationFactory;
import com.test.library.main.dto.request.BookSearchRequestDto;
import com.test.library.main.model.MasterBookWithCountedUnits;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MasterBookRepoExtension {
    Page<String> findUniqueTitles(String title, Pageable pagination);
    Page<String> findUniqueAuthors(String author, Pageable pagination);
    PaginationFactory<MasterBookWithCountedUnits> findAllWithParameters(BookSearchRequestDto parameters);
}
