package ru.practicum.ewm.category.service;

import ru.practicum.ewm.api.model.CategoryDto;
import ru.practicum.ewm.api.model.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto add(NewCategoryDto body);

    void remove(long id);

    CategoryDto update(long id, CategoryDto dto);

    CategoryDto get(long id);

    List<CategoryDto> getMany(int from, int size);
}
