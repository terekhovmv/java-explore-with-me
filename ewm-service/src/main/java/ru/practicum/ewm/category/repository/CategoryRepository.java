package ru.practicum.ewm.category.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.exception.NotFoundException;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    default Category require(long id) {
        return findById(id)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Category with id=%d was not found", id))
                );
    }

    Page<Category> findAll(Pageable pageable);
}
