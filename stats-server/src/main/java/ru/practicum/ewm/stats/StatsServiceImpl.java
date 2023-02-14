package ru.practicum.ewm.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.HitDto;
import ru.practicum.ewm.stats.dto.HitMapper;
import ru.practicum.ewm.stats.dto.SummaryDto;
import ru.practicum.ewm.stats.dto.SummaryMapper;
import ru.practicum.ewm.stats.model.App;
import ru.practicum.ewm.stats.model.Hit;
import ru.practicum.ewm.stats.model.Summary;
import ru.practicum.ewm.stats.model.Uri;
import ru.practicum.ewm.stats.repository.AppRepository;
import ru.practicum.ewm.stats.repository.HitRepository;
import ru.practicum.ewm.stats.repository.UriRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final AppRepository appRepository;
    private final UriRepository uriRepository;
    private final HitRepository hitRepository;

    private final HitMapper hitMapper;
    private final SummaryMapper summaryMapper;

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
        return hitMapper.toDto(created, app, uri);
    }

    @Override
    public List<SummaryDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uriPaths, boolean unique) {
        List<String> preparedUriPaths = uriPaths
                .stream()
                .map(this::prepareUriPath)
                .distinct()
                .collect(Collectors.toList());

        List<Uri> uris = uriRepository.findAllByPathIn(preparedUriPaths);
        if (uris.isEmpty()) {
            return List.of();
        }

        List<Long> uriIds = uris
                .stream()
                .map(Uri::getId)
                .collect(Collectors.toList());

        List<Summary> summaries = hitRepository.getSummaries(start, end, uriIds, unique);
        if (summaries.isEmpty()) {
            return List.of();
        }

        List<Short> appIds = summaries
                .stream()
                .map(Summary::getAppId)
                .distinct()
                .collect(Collectors.toList());

        Map<Short, App> appByIds = appRepository.findAllByIdIn(appIds)
                .stream()
                .collect(Collectors.toMap(App::getId, item -> item));

        Map<Long, Uri> uriByIds = uris
                .stream()
                .collect(Collectors.toMap(Uri::getId, item -> item));

        return summaries
                .stream()
                .map(item ->
                        summaryMapper.toDto(
                                item,
                                appByIds.get(item.getAppId()),
                                uriByIds.get(item.getUriId())
                        )
                )
                .collect(Collectors.toList());
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
