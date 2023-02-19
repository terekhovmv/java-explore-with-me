package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.AdminApi;
import ru.practicum.ewm.api.model.CategoryDto;
import ru.practicum.ewm.api.model.NewCategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

@Controller
@RequiredArgsConstructor
public class AdminController implements AdminApi {
    private final CategoryService service;

    @Override
    public ResponseEntity<CategoryDto> addCategory(NewCategoryDto body) {
        return new ResponseEntity<>(
                service.add(body),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<Void> deleteCategory(Long catId) {
        service.remove(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<CategoryDto> updateCategory(Long catId, CategoryDto body) {
        return new ResponseEntity<>(
                service.update(catId, body),
                HttpStatus.OK
        );
    }
}
