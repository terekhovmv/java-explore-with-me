package ru.practicum.ewm.compilation.mapping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.dto.CompilationDto;
import ru.practicum.ewm.api.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.mapping.EventMapper;
import ru.practicum.ewm.event.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public final class CompilationMapper {

    private final EventMapper eventMapper;

    public CompilationDto toDto(Compilation from) {
        return new CompilationDto()
                .id(from.getId())
                .events(
                        from.getEvents()
                                .stream()
                                .map(eventMapper::toShortDto)
                                .collect(Collectors.toList())
                )
                .pinned(from.getPinned())
                .title(from.getTitle());
    }

    public Compilation transientFromDto(NewCompilationDto from, Set<Event> events) {
        return new Compilation(
                null,
                events,
                from.isPinned(),
                from.getTitle()
        );
    }
}
