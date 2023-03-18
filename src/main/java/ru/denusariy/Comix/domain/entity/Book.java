package ru.denusariy.Comix.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.denusariy.Comix.domain.enums.Format;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(schema = "public", name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Image image;
}
