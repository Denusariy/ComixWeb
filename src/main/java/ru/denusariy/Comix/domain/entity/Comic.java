package ru.denusariy.Comix.domain.entity;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Component
@Entity
@Table(name = "comic")
public class Comic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "title")
    @NotBlank(message = "Название комикса не должно быть пустым")
    private String title;
    @Column(name = "year")
    @Min(value = 1900, message = "Год выпуска должен быть больше 1900")
    private int year;
    @Column(name = "writer")
    @NotBlank(message = "У комикса должен быть сценарист")
    private String writer;
    @Column(name = "artist")
    @NotBlank(message = "У комикса должен быть художник")
    private String artist;
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;
    public Comic() {
    }

    public Comic(String title, int year, String writer, String artist, Book book) {
        this.title = title;
        this.year = year;
        this.writer = writer;
        this.artist = artist;
        this.book = book;
    }

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

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comic comic = (Comic) o;
        return id == comic.id && year == comic.year && Objects.equals(title, comic.title) && Objects.equals(writer, comic.writer) && Objects.equals(artist, comic.artist) && Objects.equals(book, comic.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
