package ru.denusariy.Comix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denusariy.Comix.domain.entity.Image;
@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
}
