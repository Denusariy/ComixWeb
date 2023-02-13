package ru.denusariy.Comix.dao;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.denusariy.Comix.domain.entity.Book;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


@Component
public class BookDAO {
    private final EntityManager entityManager;
    @Autowired
    public BookDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public List<Book> altCover() {
        Session session = entityManager.unwrap(Session.class);
        return new ArrayList<Book>(session.createQuery("select b from Book b WHERE isAltCover = true").getResultList());
    }
    public List<Book> autograph() {
        Session session = entityManager.unwrap(Session.class);
        return new ArrayList<Book>(session.createQuery("select b from Book b WHERE isAutograph = true").getResultList());
    }
}
