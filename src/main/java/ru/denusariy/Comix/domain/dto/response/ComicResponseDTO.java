package ru.denusariy.Comix.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.denusariy.Comix.domain.entity.Artist;
import ru.denusariy.Comix.domain.entity.Book;
import ru.denusariy.Comix.domain.entity.Writer;
import java.util.List;

public class ComicResponseDTO {

    private int id;
    private String title;
    private int year;
    @JsonIgnore
    private Book book;
    private List<Writer> writers;
    private List<Artist> artists;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<Writer> getWriters() {
        return writers;
    }

    public void setWriters(List<Writer> writers) {
        this.writers = writers;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
}
