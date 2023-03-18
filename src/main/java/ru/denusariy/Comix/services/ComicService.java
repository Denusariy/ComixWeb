package ru.denusariy.Comix.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.denusariy.Comix.domain.dto.request.ComicRequestDTO;
import ru.denusariy.Comix.domain.dto.response.ComicResponseDTO;
import ru.denusariy.Comix.domain.entity.Artist;
import ru.denusariy.Comix.domain.entity.Book;
import ru.denusariy.Comix.domain.entity.Comic;
import ru.denusariy.Comix.domain.entity.Writer;
import ru.denusariy.Comix.exception.BookNotFoundException;
import ru.denusariy.Comix.exception.ComicNotFoundException;
import ru.denusariy.Comix.repositories.BookRepository;
import ru.denusariy.Comix.repositories.ComicRepository;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class ComicService {
    private final ComicRepository comicRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final WriterService writerService;
    private final ArtistService artistService;

    //сохранить комикс, сценаристов и художников
    @Transactional
    public void save(int id, ComicRequestDTO comicRequestDTO) {
        Comic comic = convertToComic(comicRequestDTO);
        comic.setWriters(writerService.save(comicRequestDTO.getWriters()));
        comic.setArtists(artistService.save(comicRequestDTO.getArtists()));
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(
                String.format("Книга с id %d не найдена!", id)));
        comic.setBook(book);
        book.setComicsList(new ArrayList<>(Collections.singletonList(comic)));
        comicRepository.save(comic);
        log.info("Сохранен комикс " + comic.getTitle());
    }

    //найти комикс по id, возвращает ResponseDTO
    @Transactional(readOnly = true)
    public ComicResponseDTO findOne(int id) {
        return convertToResponseDTO(comicRepository.findById(id).orElseThrow(() -> new ComicNotFoundException(
                String.format("Комикс с id %d не найден!", id))));
    }

    //найти комикс по id, возвращает RequestDTO. Необходимо только для Patch-запроса в Thymeleaf
    @Transactional(readOnly = true)
    public ComicRequestDTO findOneForUpdate(int id) {
        return convertToRequestDTO(comicRepository.findById(id).orElseThrow(() -> new ComicNotFoundException(
                String.format("Комикс с id %d не найден!", id))));
    }

    //обновить комикс
    @Transactional
    public void update(ComicResponseDTO updatedComic) {
        Comic comicToBeUpdated = comicRepository.findById(updatedComic.getId()).orElseThrow(() ->
                new BookNotFoundException(String.format("Комикс с id %d не найден!", updatedComic.getId())));
        modelMapper.map(updatedComic, comicToBeUpdated);
        comicRepository.save(comicToBeUpdated);
        log.info("Обновлен комикс " + comicToBeUpdated.getTitle());
    }

    //удалить комикс по id
    @Transactional
    public void delete(int comicId) {
        Comic comic = comicRepository.findById(comicId).orElseThrow(() -> new ComicNotFoundException(
                String.format("Комикс с id %d не найден!", comicId)));
        log.info("Удален комикс " + comic.getTitle());
        comicRepository.delete(comic);
    }

    //маппинг ComicRequestDTO в Comic
    public Comic convertToComic(ComicRequestDTO comicRequestDTO) {
        return modelMapper.map(comicRequestDTO, Comic.class);
    }

    //маппинг Comic в ComicResponseDTO
    public ComicResponseDTO convertToResponseDTO(Comic comic){
        return modelMapper.map(comic, ComicResponseDTO.class);
    }

    //маппинг Comic в ComicRequestDTO. Необходимо только для Patch-запроса в Thymeleaf
    public ComicRequestDTO convertToRequestDTO(Comic comic) {
        ComicRequestDTO requestDTO = modelMapper.map(comic, ComicRequestDTO.class);
        StringBuilder artists = new StringBuilder();
        for(Artist artist : comic.getArtists()) {
            artists.append(artist.toString());
            artists.append(",");
        }
        StringBuilder writers = new StringBuilder();
        for(Writer writer : comic.getWriters()) {
            writers.append(writer.toString());
            writers.append(",");
        }
        requestDTO.setArtists(artists.toString().trim());
        requestDTO.setWriters(writers.toString().trim());
        return requestDTO;
    }
}

