package ru.denusariy.Comix.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

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
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;
    @ManyToMany
    @JoinTable(
            name = "comic_writer",
            joinColumns = @JoinColumn(name = "comic_id"),
            inverseJoinColumns = @JoinColumn(name = "writer_id"))
    private List<Writer> writers;
    @ManyToMany
    @JoinTable(
            name = "comic_artist",
            joinColumns = @JoinColumn(name = "comic_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comic comic = (Comic) o;
        return id == comic.id && year == comic.year && Objects.equals(title, comic.title) && Objects.equals(book, comic.book) && Objects.equals(writers, comic.writers) && Objects.equals(artists, comic.artists);
    }
    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Comic comic = (Comic) o;
//        return id == comic.id && year == comic.year && Objects.equals(title, comic.title) && Objects.equals(writer, comic.writer) && Objects.equals(artist, comic.artist) && Objects.equals(book, comic.book);
//    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
