package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.model.CategoryDto;
import ru.practicum.ewm.api.model.NewCategoryDto;
import ru.practicum.ewm.category.mapping.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.pagination.RandomAccessPageRequest;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    private final CategoryMapper mapper;

    @Override
    public CategoryDto add(NewCategoryDto dto) {
        Category archetype = new Category(null, dto.getName());
        Category created = repository.save(archetype);
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
        if (StringUtils.isNotBlank(dto.getName())) {
            toUpdate.setName(dto.getName());
        }
        Category updated = repository.save(toUpdate);
        log.info("Category #'{}' was successfully updated", id);
        return mapper.toDto(updated);
    }

    @Override
    public CategoryDto get(long id) {
        Category found = repository.require(id);
        return mapper.toDto(found);
    }

    @Override
    public List<CategoryDto> getMany(int from, int size) {
        return repository
                .findAll(RandomAccessPageRequest.of(
                        from,
                        size,
                        Sort.by(Sort.Direction.ASC, "id")
                ))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
