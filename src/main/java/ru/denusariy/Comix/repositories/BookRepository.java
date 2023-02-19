package ru.denusariy.Comix.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denusariy.Comix.domain.entity.Book;


@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Page<Book> findByTitleContainsIgnoreCase(String title, Pageable pageable);
    Page<Book> findByIsAltCoverTrue(Pageable pageable);
    Page<Book> findByIsAutographTrue(Pageable pageable);
}
