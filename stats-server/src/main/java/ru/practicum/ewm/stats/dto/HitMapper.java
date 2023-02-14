package ru.practicum.ewm.stats.dto;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.model.App;
import ru.practicum.ewm.stats.model.Hit;
import ru.practicum.ewm.stats.model.Uri;

@Component
public class HitMapper {
    public HitDto toDto(Hit hit, App knownApp, Uri knownUri) {
        HitDto result = new HitDto();
        result.setId(hit.getId());
        App app = knownApp != null
                ? knownApp
                : hit.getApp();
        Uri uri = knownUri != null
                ? knownUri
                : hit.getUri();
        result.setApp(app.getName());
        result.setUri(uri.getPath());
        result.setIp(hit.getIp());
        result.setTimestamp(hit.getTimestamp());
        return result;
    }
}
