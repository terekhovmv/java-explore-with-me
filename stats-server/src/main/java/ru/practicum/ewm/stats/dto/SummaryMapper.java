package ru.practicum.ewm.stats.dto;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.model.App;
import ru.practicum.ewm.stats.model.Summary;
import ru.practicum.ewm.stats.model.Uri;

@Component
public class SummaryMapper {
    public SummaryDto toDto(Summary summary, App app, Uri uri) {
        SummaryDto result = new SummaryDto();
        result.setApp(app.getName());
        result.setUri(uri.getPath());
        result.setHits(summary.getHits());
        return result;
    }
}
