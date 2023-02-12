package ru.practicum.ewm.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.stats.model.Uri;

public interface UriRepository extends JpaRepository<Uri, Long> {
}
