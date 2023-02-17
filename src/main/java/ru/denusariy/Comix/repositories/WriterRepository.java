package ru.denusariy.Comix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denusariy.Comix.domain.entity.Writer;

@Repository
public interface WriterRepository extends JpaRepository<Writer, Integer> {
    Writer findByNameEquals(String name);
}
