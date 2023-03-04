package ru.practicum.ewm.category.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.api.dto.CategoryDto;
import ru.practicum.ewm.category.mapping.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.category.service.PublicCategoryService;
import ru.practicum.ewm.pagination.RandomAccessPageRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements PublicCategoryService {
    private final CategoryRepository repository;

    private final CategoryMapper mapper;

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
