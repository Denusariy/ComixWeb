package ru.denusariy.Comix.domain.entity;

import ru.denusariy.Comix.domain.enums.Format;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "title_of_book")
    @NotBlank(message = "Название книги не должно быть пустым")
    private String title;
    @Column(name = "year_of_edition")
    @Min(value = 1900, message = "Год издания должен быть больше 1900")
    private int year;
    @Enumerated(EnumType.STRING)
    @Column(name = "format")
    private Format format;
    @Column(name = "alt_cover")
    private boolean isAltCover;
    @Column(name = "autograph")
    private boolean isAutograph;
    @Column(name = "signature")
    private String signature;
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Comic> comicsList;

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

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public boolean isAltCover() {
        return isAltCover;
    }

    public void setAltCover(boolean altCover) {
        isAltCover = altCover;
    }

    public boolean isAutograph() {
        return isAutograph;
    }

    public void setAutograph(boolean autograph) {
        isAutograph = autograph;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<Comic> getComicsList() {
        return comicsList;
    }

    public void setComicsList(List<Comic> comicsList) {
        this.comicsList = comicsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && year == book.year && isAltCover == book.isAltCover && isAutograph == book.isAutograph && Objects.equals(title, book.title) && format == book.format && Objects.equals(signature, book.signature) && Objects.equals(comicsList, book.comicsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
