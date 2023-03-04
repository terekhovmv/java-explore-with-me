package ru.practicum.ewm.category.mapping;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.dto.CategoryDto;
import ru.practicum.ewm.api.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;

@Component
public final class CategoryMapper {
    public CategoryDto toDto(Category from) {
        return new CategoryDto()
                .id(from.getId())
                .name(from.getName());
    }

    public Category transientFromDto(NewCategoryDto from) {
        return new Category(
                null,
                from.getName()
        );
    }

    public void updateFromDto(Category toUpdate, CategoryDto from) {
        if (from.getName() != null) {
            toUpdate.setName(from.getName());
        }
    }
}

