package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.model.CategoryDto;
import ru.practicum.ewm.api.model.NewCategoryDto;
import ru.practicum.ewm.category.mapping.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    private final CategoryMapper mapper;

    @Override
    public CategoryDto add(NewCategoryDto dto) {
        Category archetype = new Category(null, dto.getName());
        Category created = repository.save(archetype);
        return mapper.toDto(created);
    }

    @Override
    public void remove(long id) {
        repository.require(id);
        repository.deleteById(id);
    }

    @Override
    public CategoryDto update(long id, CategoryDto dto) {
        Category toUpdate = repository.require(id);
        if (StringUtils.isNotBlank(dto.getName())) {
            toUpdate.setName(dto.getName());
        }
        Category updated = repository.save(toUpdate);
        return mapper.toDto(updated);
    }
}
