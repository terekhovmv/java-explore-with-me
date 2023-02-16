package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.stats.model.App;

import java.util.List;
import java.util.Optional;

public interface AppRepository extends JpaRepository<App, Short> {
    Optional<App> findOneByName(String name);

    List<App> findAllByIdIn(List<Short> ids);
}