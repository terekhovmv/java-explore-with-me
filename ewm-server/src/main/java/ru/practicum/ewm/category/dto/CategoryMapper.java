package ru.practicum.ewm.category.dto;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.model.Category;

@Component
public final class CategoryMapper {
    public CategoryDto toDto(Category from) {
        return new CategoryDto(
                from.getId(),
                from.getName()
        );
    }
}

