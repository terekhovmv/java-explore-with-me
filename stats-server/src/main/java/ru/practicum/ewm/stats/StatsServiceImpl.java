package ru.practicum.ewm.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.HitDto;
import ru.practicum.ewm.stats.model.App;
import ru.practicum.ewm.stats.model.Hit;
import ru.practicum.ewm.stats.model.Uri;
import ru.practicum.ewm.stats.repository.AppRepository;
import ru.practicum.ewm.stats.repository.HitRepository;
import ru.practicum.ewm.stats.repository.UriRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final AppRepository appRepository;
    private final UriRepository uriRepository;
    private final HitRepository hitRepository;

    @Override
    @Transactional
    public HitDto register(HitDto dto) {
        App app = getApp(prepareAppName(dto.getApp()));
        Uri uri = getUri(prepareUriPath(dto.getUri()));

        Hit archetype = new Hit(
                null,
                app,
                uri,
                dto.getIp(),
                dto.getTimestamp()
        );

        Hit created = hitRepository.save(archetype);
        HitDto createdDto = new HitDto();
        createdDto.setId(created.getId());
        createdDto.setApp(created.getApp().getName());
        createdDto.setUri(created.getUri().getPath());
        createdDto.setIp(created.getIp());
        createdDto.setTimestamp(created.getCreated());
        return createdDto;
    }

    private App getApp(String name) {
        return appRepository
                .findOneByName(name)
                .orElseGet(
                        () -> appRepository.save(new App(null, name))
                );
    }

    private Uri getUri(String path) {
        return uriRepository
                .findOneByPath(path)
                .orElseGet(
                        () -> uriRepository.save(new Uri(null, path))
                );
    }

    private String prepareAppName(String from) {
        return from.trim();
    }

    private String prepareUriPath(String from) {
        return from.trim();
    }
}
