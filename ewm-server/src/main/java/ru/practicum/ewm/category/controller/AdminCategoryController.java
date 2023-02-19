package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.UpdateCategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService service;

    @PostMapping
    CategoryDto add(@RequestBody @Valid UpdateCategoryDto dto) {
        return service.add(dto);
    }

    @DeleteMapping("/{id}")
    void remove(@PathVariable long id) {
        service.remove(id);
    }

    @PatchMapping("/{id}")
    CategoryDto patch(
            @PathVariable long id,
            @RequestBody UpdateCategoryDto dto
    ) {
        return service.update(id, dto);
    }
}
