package ru.denusariy.Comix.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.denusariy.Comix.domain.entity.Book;
import ru.denusariy.Comix.domain.entity.Comic;
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

    @Autowired
    public ComicService(ComicRepository comicRepository, BookRepository bookRepository, ModelMapper modelMapper) {
        this.comicRepository = comicRepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void save(int id, Comic comic) {
        comicRepository.save(comic);
        Book book = bookRepository.findById(id).orElse(null);
        comic.setBook(book);
        assert book != null;
        book.setComicsList(new ArrayList<>(Collections.singletonList(comic)));
    }

    public Comic findOne(int id) {
        return comicRepository.findById(id).orElse(null);
    }

    @Transactional
    public void update(int comicId, Comic updatedComic) {
        Comic comicToBeUpdated = comicRepository.findById(comicId).orElseThrow(ComicNotFoundException::new);
        modelMapper.map(updatedComic, comicToBeUpdated);
        comicRepository.save(comicToBeUpdated);
    }

    public int findBookByComicId(int comicId) {
        return comicRepository.findById(comicId).get().getBook().getId();
    }

    @Transactional
    public void delete(int comicId) {
        comicRepository.deleteById(comicId);
    }

    public List<String> allWriters() {
        Set<String> allWriters = new HashSet<String>();
        List<Comic> comics = comicRepository.findAll();
        for(Comic comic : comics){
            StringTokenizer tokenizer = new StringTokenizer(comic.getWriter(), ",");
            while(tokenizer.hasMoreTokens())
                allWriters.add(tokenizer.nextToken().trim());
        }
        return new ArrayList<String>(allWriters);
    }

    public List<String> allArtists() {
        Set<String> allArtists = new HashSet<String>();
        List<Comic> comics = comicRepository.findAll();
        for(Comic comic : comics){
            StringTokenizer tokenizer = new StringTokenizer(comic.getArtist(), ",");
            while(tokenizer.hasMoreTokens())
                allArtists.add(tokenizer.nextToken().trim());
        }
        return new ArrayList<String>(allArtists);
    }

    public List<Comic> findByWriter(String writer) {
        return comicRepository.findByWriterContains(writer);
    }

    public List<Comic> findByArtist(String artist) {
        return comicRepository.findByArtistContains(artist);
    }
}

