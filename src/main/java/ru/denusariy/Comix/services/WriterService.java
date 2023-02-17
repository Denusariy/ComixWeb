package ru.denusariy.Comix.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.denusariy.Comix.domain.entity.Comic;
import ru.denusariy.Comix.domain.entity.Writer;
import ru.denusariy.Comix.exception.ComicNotFoundException;
import ru.denusariy.Comix.repositories.ComicRepository;
import ru.denusariy.Comix.repositories.WriterRepository;

import java.util.*;

@Service
public class WriterService {

    private final WriterRepository writerRepository;
    private final ComicRepository comicRepository;

    public WriterService(WriterRepository writerRepository, ComicRepository comicRepository) {
        this.writerRepository = writerRepository;
        this.comicRepository = comicRepository;
    }

    @Transactional
    public List<Writer> save(String writers) {
        List<Writer> result = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(writers, ",");
        while(tokenizer.hasMoreTokens()) {
            String name = tokenizer.nextToken().trim();
            Writer writer = writerRepository.findByNameEquals(name);
            if(writer == null) {
                writer = writerRepository.save(new Writer(name));
            }
            result.add(writer);
        }
        return result;
    }
}
