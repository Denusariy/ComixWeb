package ru.denusariy.Comix.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.denusariy.Comix.domain.dto.request.BookRequestDTO;
import ru.denusariy.Comix.domain.dto.response.BookPageResponseDTO;
import ru.denusariy.Comix.domain.dto.response.BookResponseDTO;
import ru.denusariy.Comix.domain.entity.Book;
import ru.denusariy.Comix.repositories.BookRepository;
import ru.denusariy.Comix.exception.BookNotFoundException;

import java.util.List;
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

    //получить книгу по id, возвращает ResponseDTO
    @Transactional(readOnly = true)
    public BookResponseDTO findOne(int id) {
        return convertToDTO(bookRepository.findById(id).orElseThrow(BookNotFoundException::new));
    }

    //сохранить книгу, возвращает ResponseDTO
    @Transactional
    public BookResponseDTO save(BookRequestDTO bookRequestDTO) {
        return convertToDTO(bookRepository.save(convertToBook(bookRequestDTO)));
    }

    //удалить книгу по id
    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    //обновить книгу
    @Transactional
    public void update(int id, BookRequestDTO updatedBook) {
        Book bookToBeUpdated = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        modelMapper.map(updatedBook, bookToBeUpdated);
        bookRepository.save(bookToBeUpdated);
    }

    //получить список всех книг в виде ResponseDTO, с пагинацией и сортировкой по названию
    @Transactional(readOnly = true)
    public BookPageResponseDTO findAllWithPagination(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
        Page<BookResponseDTO> books = bookRepository.findAll(pageable).map(this::convertToDTO);
        List<Integer> pageNumbers = IntStream.rangeClosed(1, books.getTotalPages()).boxed().collect(Collectors.toList());
        return new BookPageResponseDTO(books, pageNumbers);
    }

    //получить список книг в виде ResponseDTO с указанной строкой в названии, с пагинацией и сортировкой по названию
    @Transactional(readOnly = true)
    public Page<BookResponseDTO> searchByTitle(String query, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
        return bookRepository.findByTitleContainsIgnoreCase(query, pageable).map(this::convertToDTO);
    }
    //получить список книг в виде ResponseDTO с альтернативной обложкой, с пагинацией и сортировкой по названию
    @Transactional(readOnly = true)
    public Page<BookResponseDTO> searchByAltCover(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
        return bookRepository.findByIsAltCoverTrue(pageable).map(this::convertToDTO);
    }

    //получить список книг в виде ResponseDTO с автографом, с пагинацией и сортировкой по названию
    @Transactional(readOnly = true)
    public Page<BookResponseDTO> searchByAutograph(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
        return bookRepository.findByIsAutographTrue(pageable).map(this::convertToDTO);
    }

    //маппинг Book в BookResponseDTO
    private BookResponseDTO convertToDTO(Book book) {
        return modelMapper.map(book, BookResponseDTO.class);
    }

    //маппинг BookRequestDTO в Book
    private Book convertToBook(BookRequestDTO bookRequestDTO) {
        return modelMapper.map(bookRequestDTO, Book.class);
    }

}
