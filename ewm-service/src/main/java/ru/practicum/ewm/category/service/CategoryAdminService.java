package ru.practicum.ewm.category.service;

import ru.practicum.ewm.api.dto.CategoryDto;
import ru.practicum.ewm.api.dto.NewCategoryDto;

public interface CategoryAdminService {
    CategoryDto add(NewCategoryDto body);

    void remove(long id);

    CategoryDto update(long id, CategoryDto dto);
}
