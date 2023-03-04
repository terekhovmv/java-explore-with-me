package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.CategoriesApi;
import ru.practicum.ewm.api.dto.CategoryDto;
import ru.practicum.ewm.category.service.PublicCategoryService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoriesApiController implements CategoriesApi {
    private final PublicCategoryService service;

    @Override
    public ResponseEntity<List<CategoryDto>> getCategories(Integer from, Integer size) {
        return new ResponseEntity<>(
                service.getMany(from, size),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<CategoryDto> getCategory(Long catId) {
        return new ResponseEntity<>(
                service.get(catId),
                HttpStatus.OK
        );
    }
}