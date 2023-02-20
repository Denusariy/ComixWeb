package ru.denusariy.Comix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.denusariy.Comix.domain.dto.request.ComicRequestDTO;
import ru.denusariy.Comix.domain.dto.response.ComicResponseDTO;
import ru.denusariy.Comix.domain.entity.Artist;
import ru.denusariy.Comix.domain.entity.Writer;


import java.util.List;

@Service
public class DeleteService {
    private final ComicService comicService;
    private final WriterService writerService;
    private final ArtistService artistService;
    private final BookService bookService;
    @Autowired
    public DeleteService(ComicService comicService, WriterService writerService, ArtistService artistService, BookService bookService) {
        this.comicService = comicService;
        this.writerService = writerService;
        this.artistService = artistService;
        this.bookService = bookService;
    }

    //конвертирует ComicRequestDTO в ComicResponseDTO и отправляет в метод update, отправляет списки сценаристов
    //и художников на проверку на удаление
    public void updateComic(int id, ComicRequestDTO updatedComic) {
        ComicResponseDTO comicToBeUpdated = comicService.findOne(id);
        List<Writer> oldWriters = comicToBeUpdated.getWriters();
        List<Artist> oldArtists = comicToBeUpdated.getArtists();
        comicToBeUpdated.setTitle(updatedComic.getTitle());
        comicToBeUpdated.setYear(updatedComic.getYear());
        comicToBeUpdated.setWriters(writerService.save(updatedComic.getWriters()));
        comicToBeUpdated.setArtists(artistService.save(updatedComic.getArtists()));
        comicService.update(comicToBeUpdated);
        writerService.deleteIfNotUsed(oldWriters);
        artistService.deleteIfNotUsed(oldArtists);
    }

    //перенаправляет запрос на удаление комикса по id, отправляет списки сценаристов и художников на проверку на удаление
    public void deleteComic(int id){
        ComicResponseDTO comicToBeUpdated = comicService.findOne(id);
        List<Writer> oldWriters = comicToBeUpdated.getWriters();
        List<Artist> oldArtists = comicToBeUpdated.getArtists();
        comicService.delete(id);
        writerService.deleteIfNotUsed(oldWriters);
        artistService.deleteIfNotUsed(oldArtists);
    }

    //перенаправляет запрос на удаление книги по id, перед этим удаляя все связанные комиксы, отправляет списки
    // сценаристов и художников на проверку на удаление
    public void deleteBook(int id) {
        bookService.findOne(id).getComicsList().forEach(comicResponseDTO -> deleteComic(comicResponseDTO.getId()));
        bookService.delete(id);
    }
}
