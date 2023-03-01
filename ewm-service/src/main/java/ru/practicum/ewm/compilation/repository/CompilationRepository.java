package ru.practicum.ewm.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.exception.NotFoundException;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    default Compilation require(long id) {
        return findById(id)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Compilation with id=%d was not found", id))
                );
    }
}
