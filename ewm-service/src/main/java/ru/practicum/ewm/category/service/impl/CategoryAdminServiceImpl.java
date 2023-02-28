package ru.practicum.ewm.category.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.CategoryDto;
import ru.practicum.ewm.api.dto.NewCategoryDto;
import ru.practicum.ewm.category.mapping.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.category.service.CategoryAdminService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryAdminServiceImpl implements CategoryAdminService {
    private final CategoryRepository repository;

    private final CategoryMapper mapper;

    @Override
    public CategoryDto add(NewCategoryDto dto) {
        Category created = repository.save(
                mapper.transientFromDto(dto)
        );
        log.info("Category '{}' was successfully added with id {}", created.getName(), created.getId());
        return mapper.toDto(created);
    }

    @Override
    public void remove(long id) {
        repository.require(id);
        repository.deleteById(id);
        log.info("Category #'{}' was successfully removed", id);
    }

    @Override
    public CategoryDto update(long id, CategoryDto dto) {
        Category toUpdate = repository.require(id);
        mapper.updateFromDto(toUpdate, dto);

        Category updated = repository.save(toUpdate);
        log.info("Category #'{}' was successfully updated", id);
        return mapper.toDto(updated);
    }
}
