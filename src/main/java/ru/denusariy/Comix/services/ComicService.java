package ru.denusariy.Comix.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.denusariy.Comix.domain.dto.request.ComicRequestDTO;
import ru.denusariy.Comix.domain.dto.response.ComicResponseDTO;
import ru.denusariy.Comix.domain.entity.Artist;
import ru.denusariy.Comix.domain.entity.Book;
import ru.denusariy.Comix.domain.entity.Comic;
import ru.denusariy.Comix.domain.entity.Writer;
import ru.denusariy.Comix.exception.ComicNotFoundException;
import ru.denusariy.Comix.repositories.BookRepository;
import ru.denusariy.Comix.repositories.ComicRepository;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ComicService {
    private final ComicRepository comicRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final WriterService writerService;
    private final ArtistService artistService;

    @Autowired
    public ComicService(ComicRepository comicRepository, BookRepository bookRepository, ModelMapper modelMapper, WriterService writerService, ArtistService artistService) {
        this.comicRepository = comicRepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
        this.writerService = writerService;
        this.artistService = artistService;
    }

    //сохранить комикс, сценаристов и художников
    @Transactional
    public void save(int id, ComicRequestDTO comicRequestDTO) {
        Comic comic = convertToComic(comicRequestDTO);
        comic.setWriters(writerService.save(comicRequestDTO.getWriters()));
        comic.setArtists(artistService.save(comicRequestDTO.getArtists()));
        Book book = bookRepository.findById(id).orElse(null);
        comic.setBook(book);
        assert book != null;
        book.setComicsList(new ArrayList<>(Collections.singletonList(comic)));
        comicRepository.save(comic);
    }

    //найти комикс по id, возвращает ResponseDTO
    @Transactional(readOnly = true)
    public ComicResponseDTO findOne(int id) {
        return convertToResponseDTO(comicRepository.findById(id).orElseThrow(ComicNotFoundException::new));
    }

    //найти комикс по id, возвращает RequestDTO. Необходимо только для Patch-запроса в Thymeleaf
    @Transactional(readOnly = true)
    public ComicRequestDTO findOneForPatch(int id) {
        return convertToRequestDTO(comicRepository.findById(id).orElseThrow(ComicNotFoundException::new));
    }

    //обновить комикс
    @Transactional
    public void update(ComicResponseDTO updatedComic) {
        Comic comicToBeUpdated = comicRepository.findById(updatedComic.getId()).orElseThrow(ComicNotFoundException::new);
        modelMapper.map(updatedComic, comicToBeUpdated);
        comicRepository.save(comicToBeUpdated);
    }

    //удалить комикс по id
    @Transactional
    public void delete(int comicId) {
        comicRepository.deleteById(comicId);
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

