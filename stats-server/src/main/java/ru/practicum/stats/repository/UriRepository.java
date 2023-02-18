package ru.practicum.stats.repository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.stats.model.Uri;

import java.util.List;
import java.util.Optional;

public interface UriRepository extends JpaRepository<Uri, Long> {
    Optional<Uri> findOneByPath(String path);

    List<Uri> findAllByPathIn(List<String> paths);

    default Uri saveIfAbsentByPath(String path) {
        return findOneByPath(path).orElseGet(
                () -> {
                    try {
                        return save(new Uri(null, path));
                    } catch (DataIntegrityViolationException exception) {
                        return findOneByPath(path).orElseThrow(() -> exception);
                    }
                }
        );
    }
}
