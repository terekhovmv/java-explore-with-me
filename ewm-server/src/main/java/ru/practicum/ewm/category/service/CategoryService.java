package ru.practicum.ewm.category.service;

import ru.practicum.ewm.api.model.CategoryDto;
import ru.practicum.ewm.api.model.NewCategoryDto;

public interface CategoryService {
    CategoryDto add(NewCategoryDto body);

    void remove(long id);

    CategoryDto update(long id, CategoryDto dto);
}
