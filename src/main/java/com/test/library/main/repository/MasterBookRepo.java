package com.test.library.main.repository;

import com.test.library.main.model.MasterBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MasterBookRepo extends JpaRepository<MasterBook, UUID>, MasterBookRepoExtension {
    Optional<MasterBook> findByTitleAndAuthor(String title, String author);
}
