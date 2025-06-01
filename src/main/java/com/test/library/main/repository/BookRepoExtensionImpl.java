package com.test.library.main.repository;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class BookRepoExtensionImpl implements BookRepoExtension {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<Book> findByIdWithCheckOutsFilteredByUnreturnedStatus(UUID bookId) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("Book_unreturnedCheckOuts");

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);

        query = query.select(root).where(cb.equal(root.get("id"), bookId));
        TypedQuery<Book> fetched = entityManager.createQuery(query);
        return fetched.getResultStream().findFirst();
    }

    @Override
    public PaginationFactory<Book> findAllWithCheckOutsFilteredByUnreturnedStatus(BookSearchRequestDto parameters) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("Book_unreturnedCheckOuts");

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);

        query = query.select(root)
                .where(createWherePredicate(cb, root, parameters))
                .orderBy(createOrdering(cb, root));
        TypedQuery<Book> fetched = entityManager.createQuery(query);

        Integer page = parameters.getPage();
        Integer limit = parameters.getPagesize();
        if (page != null && limit != null) {
            Long count = countTotal(cb, parameters);
            Pageable pageable = PageRequest.of(page, limit);
            Stream<Book> stream = fetched
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultStream();
            return new PaginationFactory<>(pageable, count, stream);
        }
        Stream<Book> stream = fetched.getResultStream();
        return new PaginationFactory<>(null, null, stream);
    }

    Long countTotal(CriteriaBuilder cb, BookSearchRequestDto parameters) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Book> countRoot = countQuery.from(Book.class);
        countQuery = countQuery
                .select(cb.count(countRoot))
                .where(createWherePredicate(cb, countRoot, parameters));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    Predicate createWherePredicate(CriteriaBuilder cb, Root<Book> root,
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

    List<Predicate> createMasterBookPredicates(CriteriaBuilder cb, Root<Book> root,
                                               String title, String author) {
        List<Predicate> predicates = new ArrayList<>();
        Join<Book, MasterBook> masterJoin = root
                .join("masterBook");

        if (title != null && !title.isBlank()) {
            predicates.add(cb.like(cb.upper(masterJoin.get("title")), likePattern(title.toUpperCase())));
        }

        if (author != null && !author.isBlank()) {
            predicates.add(cb.like(cb.upper(masterJoin.get("author")), likePattern(author.toUpperCase())));
        }
        return predicates;
    }

    List<Predicate> createCheckOutHistoryPredicates(CriteriaBuilder cb, Root<Book> root,
                                                    BookSearchRequestDto parameters) {
        List<Predicate> predicates = new ArrayList<>();
        Join<Book, CheckOutHistory> historyJoin = root
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

    List<Order> createOrdering(CriteriaBuilder cb, Root<Book> root) {
        List<Order> ordering = new ArrayList<>();
        Join<Book, CheckOutHistory> historyJoin = root
                .join("checkOutHistories", LEFT);
        ordering.add(createFieldOrdering(
                cb, historyJoin.get("checkOutDate"), Sort.Direction.DESC));
        return ordering;
    }

    public static Order createFieldOrdering(CriteriaBuilder cb,
                                            Expression<?> field,
                                            Sort.Direction direction) {
        if (direction == Sort.Direction.DESC) {
            return cb.desc(field);
        }
        return cb.asc(field);
    }

    static String likePattern(String text) {
        return "%" + text + "%";
    }
}
