package ru.denusariy.Comix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denusariy.Comix.domain.entity.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    Artist findByNameEquals(String name);
}
