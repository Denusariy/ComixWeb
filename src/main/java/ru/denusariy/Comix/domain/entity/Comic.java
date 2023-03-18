package ru.denusariy.Comix.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

@Entity
@Table(schema = "public", name = "comic")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
