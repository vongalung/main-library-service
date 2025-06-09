package com.test.library.main.repository;

import static com.test.library.main.repository.RepoExtensionHelper.createFieldOrdering;
import static com.test.library.main.repository.RepoExtensionHelper.likePattern;
import static jakarta.persistence.criteria.JoinType.LEFT;

import com.test.library.main.common.PaginationFactory;
import com.test.library.main.dto.request.BookSearchRequestDto;
import com.test.library.main.model.Book;
import com.test.library.main.model.CheckOutHistory;
import com.test.library.main.model.MasterBook;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MasterBookRepoExtensionImpl implements MasterBookRepoExtension {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    RepoExtensionHelper repoExtensionHelper;

    @Override
    public Page<String> findUniqueTitles(String title, Pageable pagination) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<MasterBook> root = query.from(MasterBook.class);

        query = query.select(root.get("title"))
                .distinct(true)
                .where(createTitlePredicate(cb, root, title))
                .orderBy(cb.asc(root.get("title")));
        TypedQuery<String> fetched = entityManager.createQuery(query);

        if (pagination == null) {
            return new PageImpl<>(fetched.getResultList());
        }
        Long count = repoExtensionHelper.countTotalDistinct(
                MasterBook.class, cb,
                r -> r.get("title"),
                (c, countRoot) -> createTitlePredicate(c, countRoot, title));
        return new PageImpl<>(fetched.getResultList(), pagination, count);
    }

    @Override
    public Page<String> findUniqueAuthors(String author, Pageable pagination) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<MasterBook> root = query.from(MasterBook.class);

        query = query.select(root.get("author"))
                .distinct(true)
                .where(createAuthorPredicate(cb, root, author))
                .orderBy(cb.asc(root.get("author")));
        TypedQuery<String> fetched = entityManager.createQuery(query);

        if (pagination == null) {
            return new PageImpl<>(fetched.getResultList());
        }
        Long count = repoExtensionHelper.countTotalDistinct(
                MasterBook.class, cb,
                r -> r.get("author"),
                (c, countRoot) -> createAuthorPredicate(c, countRoot, author));
        return new PageImpl<>(fetched.getResultList(), pagination, count);
    }

    @Override
    public PaginationFactory<MasterBook> findAllWithParameters(BookSearchRequestDto parameters) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("Book_unreturnedCheckOuts");

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<MasterBook> query = cb.createQuery(MasterBook.class);
        Root<MasterBook> root = query.from(MasterBook.class);

        query = query.select(root)
                .where(createWherePredicate(cb, root, parameters))
                .orderBy(createOrdering(cb, root));
        TypedQuery<MasterBook> fetched = entityManager.createQuery(query);

        Integer page = parameters.getPage();
        Integer limit = parameters.getPagesize();
        if (page != null && limit != null) {
            Long count = repoExtensionHelper.countTotal(MasterBook.class, cb,
                    (c, countRoot) -> createWherePredicate(c, countRoot, parameters));
            Pageable pageable = PageRequest.of(page, limit);
            Stream<MasterBook> stream = fetched
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultStream();
            return new PaginationFactory<>(pageable, count, stream);
        }
        Stream<MasterBook> stream = fetched.getResultStream();
        return new PaginationFactory<>(null, null, stream);
    }

    Predicate createWherePredicate(CriteriaBuilder cb, Root<MasterBook> root,
                                   BookSearchRequestDto parameters) {
        List<Predicate> predicates = new ArrayList<>();

        String title = parameters.getTitle();
        String author = parameters.getAuthor();
        if ((title != null && !title.isBlank()) || (author != null && !author.isBlank())) {
            predicates.addAll(createMasterBookPredicates(cb, root, title, author));
        }
        predicates.addAll(createCheckOutHistoryPredicates(cb, root, parameters));

        return cb.and(predicates.toArray(new Predicate[0]));
    }

    List<Predicate> createMasterBookPredicates(CriteriaBuilder cb, Root<MasterBook> root,
                                               String title, String author) {
        List<Predicate> predicates = new ArrayList<>();
        if (title != null && !title.isBlank()) {
            predicates.add(createTitlePredicate(cb, root, title));
        }

        if (author != null && !author.isBlank()) {
            predicates.add(createAuthorPredicate(cb, root, author));
        }
        return predicates;
    }

    Predicate createTitlePredicate(CriteriaBuilder cb, Root<MasterBook> root, String title) {
        return cb.like(cb.upper(root.get("title")), likePattern(title.toUpperCase()));
    }

    Predicate createAuthorPredicate(CriteriaBuilder cb, Root<MasterBook> root, String author) {
        return cb.like(cb.upper(root.get("author")), likePattern(author.toUpperCase()));
    }

    List<Predicate> createCheckOutHistoryPredicates(CriteriaBuilder cb, Root<MasterBook> root,
                                                    BookSearchRequestDto parameters) {
        List<Predicate> predicates = new ArrayList<>();
        Join<MasterBook, Book> bookJoin = root.join("books", LEFT);
        Join<Book, CheckOutHistory> historyJoin = bookJoin
                .join("checkOutHistories", LEFT);

        if (parameters.getIsUnreturned() != null && parameters.getIsUnreturned()) {
            predicates.add(cb.isNotNull(historyJoin.get("checkOutDate")));
        }
        if (parameters.getCheckOutStart() != null) {
            predicates.add(cb.greaterThanOrEqualTo(
                    historyJoin.get("checkOutDate"), parameters.getCheckOutStart()));
        }
        if (parameters.getCheckOutEnd() != null) {
            predicates.add(cb.lessThanOrEqualTo(
                    historyJoin.get("checkOutDate"), parameters.getCheckOutEnd()));
        }
        if (parameters.getExpectedReturnStart() != null) {
            predicates.add(cb.greaterThanOrEqualTo(
                    historyJoin.get("expectedReturnDate"), parameters.getExpectedReturnStart()));
        }
        if (parameters.getExpectedReturnEnd() != null) {
            predicates.add(cb.lessThanOrEqualTo(
                    historyJoin.get("expectedReturnDate"), parameters.getExpectedReturnEnd()));
        }
        if (parameters.getReturnStart() != null) {
            predicates.add(cb.greaterThanOrEqualTo(
                    historyJoin.get("returnDate"), parameters.getReturnStart()));
        }
        if (parameters.getReturnEnd() != null) {
            predicates.add(cb.lessThanOrEqualTo(
                    historyJoin.get("returnDate"), parameters.getReturnEnd()));
        }

        return predicates;
    }

    List<Order> createOrdering(CriteriaBuilder cb, Root<MasterBook> root) {
        List<Order> ordering = new ArrayList<>();
        ordering.add(createFieldOrdering(
                cb, root.get("title"), Direction.ASC));
        return ordering;
    }
}
