package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.stats.model.Uri;

import java.util.List;
import java.util.Optional;

public interface UriRepository extends JpaRepository<Uri, Long> {
    Optional<Uri> findOneByPath(String path);

    List<Uri> findAllByPathIn(List<String> paths);
}
