package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.RegisterHitDto;
import ru.practicum.stats.dto.SummaryDto;
import ru.practicum.stats.model.App;
import ru.practicum.stats.model.Hit;
import ru.practicum.stats.model.Summary;
import ru.practicum.stats.model.Uri;
import ru.practicum.stats.repository.AppRepository;
import ru.practicum.stats.repository.HitRepository;
import ru.practicum.stats.repository.UriRepository;

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

    @Override
    public HitDto registerHit(RegisterHitDto dto) {
        App app = appRepository.saveIfAbsentByName(prepareAppName(dto.getApp()));
        Uri uri = uriRepository.saveIfAbsentByPath(prepareUriPath(dto.getUri()));

        Hit archetype = new Hit(
                null,
                app,
                uri,
                dto.getIp(),
                dto.getTimestamp()
        );

        Hit created = hitRepository.save(archetype);
        return new HitDto(
                created.getId(),
                app.getName(),
                uri.getPath(),
                created.getIp(),
                created.getTimestamp()
        );
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
                        new SummaryDto(
                                appByIds.get(item.getAppId()).getName(),
                                uriByIds.get(item.getUriId()).getPath(),
                                item.getHits()
                        )
                )
                .collect(Collectors.toList());
    }

    private String prepareAppName(String from) {
        return from.trim();
    }

    private String prepareUriPath(String from) {
        return from.trim();
    }
}
