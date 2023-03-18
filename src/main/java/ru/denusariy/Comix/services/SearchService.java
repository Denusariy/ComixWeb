package ru.denusariy.Comix.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.denusariy.Comix.domain.dto.response.*;
import ru.denusariy.Comix.domain.entity.Artist;
import ru.denusariy.Comix.domain.entity.Comic;
import ru.denusariy.Comix.domain.entity.Writer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {
    private final BookService bookService;
    private final ComicService comicService;
    private final WriterService writerService;
    private final ArtistService artistService;

    //получить ResponseDTO со списком всех сценаристов и художников
    public ArtistsWritersResponseDTO getArtistWriterResponseDTO() {
        ArtistsWritersResponseDTO response = new ArtistsWritersResponseDTO();
        response.setArtists(artistService.findAll());
        response.setWriters(writerService.findAll());
        return response;
    }

    //получить в виде ResponseDTO список книг с указанной строкой в названии, с пагинацией и сортировкой по названию
    public BookPageResponseDTO findBooksByTitle(String title, Integer page, Integer size) {
        Page<BookResponseDTO> books = bookService.searchByTitle(title, page, size);
        List<Integer> pageNumbers = IntStream.rangeClosed(1, books.getTotalPages()).boxed().collect(Collectors.toList());
        return new BookPageResponseDTO(books, pageNumbers);
    }

    //получить в виде ResponseDTO список книг с альтернативной обложкой, с пагинацией и сортировкой по названию
    public BookPageResponseDTO findBooksWithAltCover(Integer page, Integer size) {
        Page<BookResponseDTO> books = bookService.searchByAltCover(page, size);
        List<Integer> pageNumbers = IntStream.rangeClosed(1, books.getTotalPages()).boxed().collect(Collectors.toList());
        return new BookPageResponseDTO(books, pageNumbers);
    }

    //получить в виде ResponseDTO список книг с автографом, с пагинацией и сортировкой по названию
    public BookPageResponseDTO findBooksWithAutograph(Integer page, Integer size) {
        Page<BookResponseDTO> books = bookService.searchByAutograph(page, size);
        List<Integer> pageNumbers = IntStream.rangeClosed(1, books.getTotalPages()).boxed().collect(Collectors.toList());
        return new BookPageResponseDTO(books, pageNumbers);
    }

    //получить в виде ResponseDTO список комиксов с указанным сценаристом, с пагинацией и сортировкой по названию
    public WriterResponseDTO findComicsByWriter(String name, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Writer writer = writerService.findByName(name);
        Page<ComicResponseDTO> comics = findPaginated(
                writer.getComics().stream().sorted(Comparator.comparing(Comic::getTitle)).collect(Collectors.toList()),
                pageable);
        WriterResponseDTO response = new WriterResponseDTO();
        response.setName(writer.getName());
        response.setComics(comics);
        response.setPageNumbers(IntStream.rangeClosed(1, comics.getTotalPages()).boxed().collect(Collectors.toList()));
        return response;
    }

    //получить в виде ResponseDTO список комиксов с указанным художником, с пагинацией и сортировкой по названию
    public ArtistResponseDTO findComicsByArtist(String name, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Artist artist = artistService.findByName(name);
        Page<ComicResponseDTO> comics = findPaginated(
                artist.getComics().stream().sorted(Comparator.comparing(Comic::getTitle)).collect(Collectors.toList()),
                pageable);
        ArtistResponseDTO response = new ArtistResponseDTO();
        response.setName(artist.getName());
        response.setComics(comics);
        response.setPageNumbers(IntStream.rangeClosed(1, comics.getTotalPages()).boxed().collect(Collectors.toList()));
        return response;
    }

    //метод для пагинации списка комиксов
    public Page<ComicResponseDTO> findPaginated(List<Comic> comics, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Comic> list;
        if(comics.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, comics.size());
            list = comics.subList(startItem, toIndex);
        }
        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), comics.size())
                .map(comicService::convertToResponseDTO);
    }

}
