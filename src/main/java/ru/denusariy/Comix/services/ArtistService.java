package ru.denusariy.Comix.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.denusariy.Comix.domain.entity.Artist;
import ru.denusariy.Comix.repositories.ArtistRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
@Log4j2
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;

    //сохранение художников, преобразование из строки в список сущностей
    @Transactional
    public List<Artist> save(String artists) {
        List<Artist> result = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(artists, ",");
        while(tokenizer.hasMoreTokens()) {
            String name = tokenizer.nextToken().trim();
            Artist artist = artistRepository.findByNameEquals(name);
            if(artist == null) {
                artist = artistRepository.save(new Artist(name));
                log.info("Сохранен художник " + artist.getName());
            }
            result.add(artist);
        }
        return result;
    }

    //получение списка всех художников, с сортировкой
    @Transactional(readOnly = true)
    public List<Artist> findAll() {
        return artistRepository.findAll(Sort.by("name"));
    }

    //получение художника по имени
    @Transactional(readOnly = true)
    public Artist findByName(String name) {
        String artist = name.replace('+', ' ');
        return artistRepository.findByNameEquals(artist);
    }

    //удаление из таблицы неиспользуемых художников
    @Transactional
    public void deleteIfNotUsed(List<Artist> oldArtists) {
        for(Artist artist : oldArtists) {
            if(artist.getComics().isEmpty()) {
                log.info("Удален художник " + artist.getName());
                artistRepository.delete(artist);
            }
        }
    }
}
