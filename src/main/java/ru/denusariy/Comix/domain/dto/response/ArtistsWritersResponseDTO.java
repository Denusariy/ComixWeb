package ru.denusariy.Comix.domain.dto.response;

import ru.denusariy.Comix.domain.entity.Artist;
import ru.denusariy.Comix.domain.entity.Writer;

import java.util.List;

public class ArtistsWritersResponseDTO {
    private List<Writer> writers;
    private List<Artist> artists;

    public List<Writer> getWriters() {
        return writers;
    }

    public void setWriters(List<Writer> writers) {
        this.writers = writers;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
}
