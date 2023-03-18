package ru.denusariy.Comix.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.denusariy.Comix.domain.entity.Writer;
import ru.denusariy.Comix.repositories.WriterRepository;

import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class WriterService {
    private final WriterRepository writerRepository;

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
                log.info("Сохранен сценарист " + writer.getName());
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
            if(writer.getComics().isEmpty()) {
                log.info("Удален сценарист " + writer.getName());
                writerRepository.delete(writer);
            }
        }
    }
}
