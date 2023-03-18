package ru.denusariy.Comix.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.denusariy.Comix.domain.dto.request.BookRequestDTO;
import ru.denusariy.Comix.domain.dto.response.BookPageResponseDTO;
import ru.denusariy.Comix.domain.dto.response.BookResponseDTO;
import ru.denusariy.Comix.domain.entity.Book;
import ru.denusariy.Comix.domain.entity.Image;
import ru.denusariy.Comix.repositories.BookRepository;
import ru.denusariy.Comix.exception.BookNotFoundException;
import ru.denusariy.Comix.repositories.ImageRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@Log4j2
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    //получить книгу по id, возвращает ResponseDTO
    @Transactional(readOnly = true)
    public BookResponseDTO findOne(int id) {
        return convertToDTO(bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(
                String.format("Книга с id %d не найдена!", id))));
    }
    //получить книгу по id, возвращает RequestDTO
    @Transactional(readOnly = true)
    public BookRequestDTO findOneForUpdate(int id) {
        return convertToRequestDTO(bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(
                String.format("Книга с id %d не найдена!", id))));
    }

    //сохранить книгу и изображение, возвращает ResponseDTO
    @Transactional
    public BookResponseDTO save(BookRequestDTO bookRequestDTO, MultipartFile file) throws IOException {
        Book newBook = convertToBook(bookRequestDTO);
        if (file.getSize() != 0) {
            Image image = toImageEntity(file);
            newBook.setImage(image);
            image.setBook(newBook);
            imageRepository.save(image);
            log.info("Сохранено изображение " + image.getOriginalName());
        }
        log.info("Сохранена книга " + newBook.getTitle());
        return convertToDTO(bookRepository.save(newBook));
    }

    //маппинг MultipartFile в Image
    private Image toImageEntity(MultipartFile file) throws IOException {
        return Image.builder()
                .originalName(file.getOriginalFilename())
                .size(file.getSize())
                .contentType(file.getContentType())
                .bytes(file.getBytes())
                .build();
    }

    //удалить книгу по id
    @Transactional
    public void delete(int id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(
                String.format("Книга с id %d не найдена!", id)));
        log.info("Удалена книга " + book.getTitle());
        bookRepository.delete(book);
    }

    //обновить книгу и сохранить изображение
    @Transactional
    public void update(int id, BookRequestDTO updatedBook, MultipartFile file) throws IOException {
        Book bookToBeUpdated = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(
                String.format("Книга с id %d не найдена!", id)));
        Book book = Book.builder()
                .id(bookToBeUpdated.getId())
                .comicsList(bookToBeUpdated.getComicsList())
                .image(bookToBeUpdated.getImage())
                .title(updatedBook.getTitle())
                .year(updatedBook.getYear())
                .format(updatedBook.getFormat())
                .isAltCover(updatedBook.isAltCover())
                .isAutograph(updatedBook.isAutograph())
                .signature(updatedBook.getSignature())
                .build();
        if (file.getSize() != 0) {
            Image image = toImageEntity(file);
            Image oldImage = bookToBeUpdated.getImage();
            if(oldImage != null) {
                log.info("Удалено изображение " + oldImage.getOriginalName());
                imageRepository.delete(oldImage);
            }
            book.setImage(image);
            image.setBook(book);
            imageRepository.save(image);
            log.info("Сохранено изображение " + image.getOriginalName());
        }
        bookRepository.save(book);
        log.info("Обновлена книга " + book.getTitle());
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

    //маппинг Book в BookRequestDTO
    private BookRequestDTO convertToRequestDTO(Book book) {
        return modelMapper.map(book, BookRequestDTO.class);
    }

}
