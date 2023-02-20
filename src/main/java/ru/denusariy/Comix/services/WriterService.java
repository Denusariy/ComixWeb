package ru.denusariy.Comix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.denusariy.Comix.domain.entity.Writer;
import ru.denusariy.Comix.repositories.WriterRepository;

import java.util.*;

@Service
public class WriterService {

    private final WriterRepository writerRepository;
    @Autowired
    public WriterService(WriterRepository writerRepository) {
        this.writerRepository = writerRepository;
    }

    //сохранение сценаристов, преобразование из строки в список сущностей
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
    //получение списка всех сценаристов, с сортировкой
    @Transactional(readOnly = true)
    public List<Writer> findAll() {
        return writerRepository.findAll(Sort.by("name"));
    }


    //получение сценариста по имени
    @Transactional(readOnly = true)
    public Writer findByName(String name) {
        String writer = name.replace('+', ' ');
        return writerRepository.findByNameEquals(writer);
    }

    //удаление из таблицы неиспользуемых сценаристов
    @Transactional
    public void deleteIfNotUsed(List<Writer> oldWriters) {
        for(Writer writer : oldWriters) {
            if(writer.getComics().isEmpty())
                writerRepository.delete(writer);
        }
    }
}
