package ru.practicum.ewm.category.service;

import ru.practicum.ewm.api.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {
    CategoryDto get(long id);

    List<CategoryDto> getMany(int from, int size);
}
