package com.test.library.main.repository;

import com.test.library.main.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import java.util.Optional;
import java.util.UUID;

public class UserRepoExtensionImpl implements UserRepoExtension {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<User> findByIdWithCheckOutsFilteredByUnreturnedStatus(UUID userId) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("Book_unreturnedCheckOuts");

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        query = query.select(root).where(cb.equal(root.get("id"), userId));
        TypedQuery<User> fetched = entityManager.createQuery(query);
        return fetched.getResultStream().findFirst();
    }
}
