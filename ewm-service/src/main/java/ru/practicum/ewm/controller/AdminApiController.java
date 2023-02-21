package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.AdminApi;
import ru.practicum.ewm.api.model.*;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.user.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminApiController implements AdminApi {
    private final CategoryService categoryService;

    private final UserService userService;

    @Override
    public ResponseEntity<CategoryDto> addCategory(NewCategoryDto body) {
        return new ResponseEntity<>(
                categoryService.add(body),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<Void> deleteCategory(Long catId) {
        categoryService.remove(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<CategoryDto> updateCategory(Long catId, CategoryDto body) {
        return new ResponseEntity<>(
                categoryService.update(catId, body),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<UserDto> registerUser(NewUserRequest body) {
        return new ResponseEntity<>(
                userService.add(body),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long userId) {
        userService.remove(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<UserDto>> getUsers(List<Long> ids, Integer from, Integer size) {
        return new ResponseEntity<>(
                userService.getByIds(ids, from, size),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<EventFullDto>> getEvents2(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, Integer from, Integer size) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<EventFullDto> updateEvent1(Long eventId, UpdateEventAdminRequest body) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<CompilationDto> saveCompilation(NewCompilationDto body) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<CompilationDto> updateCompilation(Long compId, UpdateCompilationRequest body) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<Void> deleteCompilation(Long compId) {
        //TODO
        throw new UnsupportedOperationException();
    }
}
