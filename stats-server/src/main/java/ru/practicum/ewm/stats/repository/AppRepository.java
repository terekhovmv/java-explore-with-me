package ru.practicum.ewm.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.stats.model.App;

import java.util.Optional;

public interface AppRepository extends JpaRepository<App, Short> {
    Optional<App> findOneByName(String name);
}
