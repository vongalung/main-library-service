package com.test.library.main.repository;

import static com.test.library.main.repository.RepoExtensionHelper.createFieldOrdering;
import static jakarta.persistence.criteria.JoinType.LEFT;

import com.test.library.main.common.PaginationFactory;
import com.test.library.main.model.Book;
import com.test.library.main.model.CheckOutHistory;
import com.test.library.main.model.MasterBook;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class BookRepoExtensionImpl implements BookRepoExtension {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    RepoExtensionHelper repoExtensionHelper;

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
    public PaginationFactory<Book> findByMasterId(UUID masterId, Pageable pagination) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("Book_unreturnedCheckOuts");

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);
        Join<Book, MasterBook> masterJoin = root.join("masterBook");

        query = query.select(root)
                .where(cb.equal(masterJoin.get("id"), masterId))
                .orderBy(createOrdering(cb, root));
        TypedQuery<Book> fetched = entityManager.createQuery(query);

        if (pagination == null) {
            Stream<Book> stream = fetched.getResultStream();
            return new PaginationFactory<>(null, null, stream);
        }

        Long count = repoExtensionHelper.countTotal(Book.class, cb,
                (c, countRoot) -> c.equal(countRoot.get("id"), masterId));
        Stream<Book> stream = fetched
                .setFirstResult((int) pagination.getOffset())
                .setMaxResults(pagination.getPageSize())
                .getResultStream();
        return new PaginationFactory<>(pagination, count, stream);
    }

    List<Order> createOrdering(CriteriaBuilder cb, Root<Book> root) {
        List<Order> ordering = new ArrayList<>();
        Join<Book, CheckOutHistory> historyJoin = root
                .join("checkOutHistories", LEFT);
        ordering.add(createFieldOrdering(
                cb, historyJoin.get("checkOutDate"), Direction.DESC));
        return ordering;
    }
}
