package ru.denusariy.Comix.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import ru.denusariy.Comix.domain.dto.request.ComicRequestDTO;
import ru.denusariy.Comix.domain.dto.response.ComicResponseDTO;
import ru.denusariy.Comix.domain.entity.Artist;
import ru.denusariy.Comix.domain.entity.Book;
import ru.denusariy.Comix.domain.entity.Comic;
import ru.denusariy.Comix.domain.entity.Writer;
import ru.denusariy.Comix.exception.ComicNotFoundException;
import ru.denusariy.Comix.repositories.BookRepository;
import ru.denusariy.Comix.repositories.ComicRepository;

import java.lang.reflect.Field;
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

    @Transactional
    public ComicResponseDTO save(int id, ComicRequestDTO comicRequestDTO) {
        Comic comic = convertToComic(comicRequestDTO);
        comic.setWriters(writerService.save(comicRequestDTO.getWriters()));
        comic.setArtists(artistService.save(comicRequestDTO.getArtists()));
        Book book = bookRepository.findById(id).orElse(null);
        comic.setBook(book);
        assert book != null;
        book.setComicsList(new ArrayList<>(Collections.singletonList(comic)));
        return convertToResponseDTO(comicRepository.save(comic));
    }
    @Transactional(readOnly = true)
    public ComicResponseDTO findOne(int id) {
        return convertToResponseDTO(comicRepository.findById(id).orElseThrow(ComicNotFoundException::new));
    }

    @Transactional(readOnly = true)
    public ComicRequestDTO findOneForPatch(int id) {
        return convertToRequestDTO(comicRepository.findById(id).orElseThrow(ComicNotFoundException::new));
    }

    @Transactional
    public void update(int comicId, ComicRequestDTO comicRequestDTO) {
        Comic comicToBeUpdated = comicRepository.findById(comicId).orElseThrow(ComicNotFoundException::new);
        comicToBeUpdated.setTitle(comicRequestDTO.getTitle());
        comicToBeUpdated.setYear(comicRequestDTO.getYear());
        comicToBeUpdated.setWriters(writerService.save(comicRequestDTO.getWriters()));
        comicToBeUpdated.setArtists(artistService.save(comicRequestDTO.getArtists()));

//        modelMapper.map(updatedComic, comicToBeUpdated);
        comicRepository.save(comicToBeUpdated);
    }

    @Transactional
    public void delete(int comicId) {
        comicRepository.deleteById(comicId);
    }

//    public List<String> allWriters() {
//        Set<String> allWriters = new HashSet<String>();
//        List<Comic> comics = comicRepository.findAll();
//        for(Comic comic : comics){
//            StringTokenizer tokenizer = new StringTokenizer(comic.getWriter(), ",");
//            while(tokenizer.hasMoreTokens())
//                allWriters.add(tokenizer.nextToken().trim());
//        }
//        return new ArrayList<String>(allWriters);
//    }
//
//    public List<String> allArtists() {
//        Set<String> allArtists = new HashSet<String>();
//        List<Comic> comics = comicRepository.findAll();
//        for(Comic comic : comics){
//            StringTokenizer tokenizer = new StringTokenizer(comic.getArtist(), ",");
//            while(tokenizer.hasMoreTokens())
//                allArtists.add(tokenizer.nextToken().trim());
//        }
//        return new ArrayList<String>(allArtists);
//    }

//    public List<Comic> findByWriter(String writer) {
//        return comicRepository.findByWriterContains(writer);
//    }
//
//    public List<Comic> findByArtist(String artist) {
//        return comicRepository.findByArtistContains(artist);
//    }

    private Comic convertToComic(ComicRequestDTO comicRequestDTO) {
        return modelMapper.map(comicRequestDTO, Comic.class);
    }

    private ComicResponseDTO convertToResponseDTO(Comic comic){
        return modelMapper.map(comic, ComicResponseDTO.class);
    }

    private ComicRequestDTO convertToRequestDTO(Comic comic) {
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

