package ru.practicum.ewm.compilation.repository;

import ru.practicum.ewm.compilation.model.Compilation;

import java.util.List;

public interface CustomCompilationRepository {

    List<Compilation> find(
            Boolean pinned,
            int from,
            int size
    );
}
