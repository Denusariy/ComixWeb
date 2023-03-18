package ru.denusariy.Comix.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(schema = "public", name = "writer")
@Getter
@Setter
@NoArgsConstructor
public class Writer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @Column(name = "name")
    @NotBlank
    String name;
    @ManyToMany(mappedBy = "writers")
    List<Comic> comics;

    public Writer(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return " " + name;
    }
}
