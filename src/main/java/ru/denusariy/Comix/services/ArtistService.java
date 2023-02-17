package ru.denusariy.Comix.services;

import org.springframework.stereotype.Service;
import ru.denusariy.Comix.domain.entity.Artist;
import ru.denusariy.Comix.domain.entity.Writer;
import ru.denusariy.Comix.repositories.ArtistRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
public class ArtistService {
    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<Artist> save(String artists) {
        List<Artist> result = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(artists, ",");
        while(tokenizer.hasMoreTokens()) {
            String name = tokenizer.nextToken().trim();
            Artist artist = artistRepository.findByNameEquals(name);
            if(artist == null) {
                artist = artistRepository.save(new Artist(name));
            }
            result.add(artist);
        }
        return result;
    }
}
