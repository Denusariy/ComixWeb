package ru.denusariy.Comix.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(schema = "public", name = "image")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    @Column(name = "original_name")
    private String  originalName;
    @Column(name = "size")
    private Long size;
    @Column(name = "content_type")
    private String contentType;
    @Column(columnDefinition = "bytea", name = "bytes")  //тип хранения в БД. Есть аннотация @Lob для этого же, но нужна зависимость
    private byte[] bytes;
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

}
