package ru.denusariy.Comix.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.denusariy.Comix.domain.entity.Book;
import ru.denusariy.Comix.domain.entity.Comic;
import ru.denusariy.Comix.repositories.BookRepository;
import ru.denusariy.Comix.exception.BookNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    @Autowired
    public BookService(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public Book findOne(int id) {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Comic> getComicsByBookId(int id) {
        return bookRepository.findById(id).get().getComicsList();
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book bookToBeUpdated = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        modelMapper.map(updatedBook, bookToBeUpdated);
        bookRepository.save(bookToBeUpdated);
    }

    @Transactional(readOnly = true)
    public List<Book> searchByTitle(String query) {
        return bookRepository.findByTitleContainsIgnoreCase(query);
    }

    public Page<Book> findWithPagination(Optional<Integer> page, Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);
        return findPaginated(PageRequest.of(currentPage - 1, pageSize));
    }

    @Transactional(readOnly = true)
    public Page<Book> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Book> list;
        List<Book> books = bookRepository.findAll(Sort.by("title"));
        if(books.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, books.size());
            list = books.subList(startItem, toIndex);
        }
        Page<Book> bookPage = new PageImpl<Book>(list, PageRequest.of(currentPage, pageSize), books.size());
        return bookPage;
    }

    public List<Integer> getPageNumbers(Page<Book> bookPage) {
        return IntStream.rangeClosed(1, bookPage.getTotalPages()).boxed().collect(Collectors.toList());
    }

}
