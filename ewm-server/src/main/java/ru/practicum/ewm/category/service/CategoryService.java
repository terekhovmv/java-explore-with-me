package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.UpdateCategoryDto;

public interface CategoryService {
    CategoryDto add(UpdateCategoryDto dto);

    void remove(long id);

    CategoryDto update(long id, UpdateCategoryDto dto);
}
