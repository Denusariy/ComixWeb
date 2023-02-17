package ru.denusariy.Comix.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.denusariy.Comix.domain.dto.request.BookRequestDTO;
import ru.denusariy.Comix.domain.dto.response.BookResponseDTO;
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
    public BookResponseDTO findOne(int id) {
        return convertToDTO(bookRepository.findById(id).orElseThrow(BookNotFoundException::new));
    }

    @Transactional(readOnly = true)
    public BookRequestDTO findOneForPatch(int id) {
        return convertToRequestDTO(bookRepository.findById(id).orElseThrow(BookNotFoundException::new));
    }

    @Transactional
    public BookResponseDTO save(BookRequestDTO bookRequestDTO) {
        return convertToDTO(bookRepository.save(convertToBook(bookRequestDTO)));
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public BookResponseDTO update(int id, BookRequestDTO updatedBook) {
        Book bookToBeUpdated = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        modelMapper.map(updatedBook, bookToBeUpdated);
        return convertToDTO(bookRepository.save(bookToBeUpdated));
    }
//
//    @Transactional(readOnly = true)
//    public List<Book> searchByTitle(String query) {
//        return bookRepository.findByTitleContainsIgnoreCase(query);
//    }

    public Page<BookResponseDTO> findWithPagination(Optional<Integer> page, Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);
        return findPaginated(PageRequest.of(currentPage - 1, pageSize));
    }

    @Transactional(readOnly = true)
    public Page<BookResponseDTO> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<BookResponseDTO> list;
        List<BookResponseDTO> books = bookRepository.findAll(Sort.by("title")).stream()
                .map(this::convertToDTO).collect(Collectors.toList());
        if(books.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, books.size());
            list = books.subList(startItem, toIndex);
        }
        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), books.size());
    }

    public List<Integer> getPageNumbers(Page<BookResponseDTO> bookPage) {
        return IntStream.rangeClosed(1, bookPage.getTotalPages()).boxed().collect(Collectors.toList());
    }

    private BookResponseDTO convertToDTO(Book book) {
        return modelMapper.map(book, BookResponseDTO.class);
    }

    private Book convertToBook(BookRequestDTO bookRequestDTO) {
        return modelMapper.map(bookRequestDTO, Book.class);
    }

    private BookRequestDTO convertToRequestDTO(Book book) {
        return modelMapper.map(book, BookRequestDTO.class);
    }

}
