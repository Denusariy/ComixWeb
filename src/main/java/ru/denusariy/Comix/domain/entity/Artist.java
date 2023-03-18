package ru.denusariy.Comix.domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(schema = "public", name = "artist")
@Getter
@Setter
@NoArgsConstructor
public class Artist {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    @Column(name = "name")
    private String name;
    @ManyToMany(mappedBy = "artists")
    private List<Comic> comics;

    public Artist(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return " " + name;
    }
}
