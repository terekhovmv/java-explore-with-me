package ru.practicum.stats.repository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.stats.model.App;

import java.util.List;
import java.util.Optional;

public interface AppRepository extends JpaRepository<App, Short> {
    Optional<App> findOneByName(String name);

    List<App> findAllByIdIn(List<Short> ids);

    default App saveIfAbsentByName(String name) {
        return findOneByName(name).orElseGet(
                () -> {
                    try {
                        return save(new App(null, name));
                    } catch (DataIntegrityViolationException exception) {
                        return findOneByName(name).orElseThrow(() -> exception);
                    }
                }
        );
    }
}
