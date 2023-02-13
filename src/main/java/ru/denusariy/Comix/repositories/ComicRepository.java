package ru.denusariy.Comix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denusariy.Comix.domain.entity.Comic;

import java.util.List;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Integer> {
    List<Comic> findByWriterContains(String writer);
    List<Comic> findByArtistContains(String artist);
}
