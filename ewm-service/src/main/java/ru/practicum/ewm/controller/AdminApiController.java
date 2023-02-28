package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.AdminApi;
import ru.practicum.ewm.api.dto.*;
import ru.practicum.ewm.api.dto.validation.*;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.event.service.EventAdminService;
import ru.practicum.ewm.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminApiController implements AdminApi {
    private final CategoryService categoryService;

    private final UserService userService;

    private final EventAdminService eventAdminService;

    private final NewCategoryDtoValidator newCategoryDtoValidator;

    private final UpdateCategoryDtoValidator updateCategoryDtoValidator;

    private final NewUserDtoValidator newUserDtoValidator;

    private final UpdateEventAdminDtoValidator updateEventAdminDtoValidator;

    private final StringDateTimeValidator stringDateTimeValidator;

    private final StringStateEnumValidator stringStateEnumValidator;

    private final RandomAccessPageRequestValidator randomAccessPageRequestValidator;

    @Override
    public ResponseEntity<CategoryDto> addCategory(NewCategoryDto body) {
        newCategoryDtoValidator.requireValid(body);

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
        updateCategoryDtoValidator.requireValid(body);

        return new ResponseEntity<>(
                categoryService.update(catId, body),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<UserDto> registerUser(NewUserDto body) {
        newUserDtoValidator.requireValid(body);

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
    public ResponseEntity<List<EventFullDto>> getEventsAdmin(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, Integer from, Integer size) {
        List<EventFullDto.StateEnum> filterStates = stringStateEnumValidator.requireValidOrNull(states, "states");
        LocalDateTime filterStart = stringDateTimeValidator.requireValidOrNull(rangeStart, "rangeStart");
        LocalDateTime filterEnd = stringDateTimeValidator.requireValidOrNull(rangeEnd, "rangeEnd");
        randomAccessPageRequestValidator.requireValid(from, size);

        return new ResponseEntity<>(
                eventAdminService.find(
                        users,
                        filterStates,
                        categories,
                        filterStart,
                        filterEnd,
                        from,
                        size
                ),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<EventFullDto> updateEvent1(Long eventId, UpdateEventAdminDto body) {
        updateEventAdminDtoValidator.requireValid(body);

        return new ResponseEntity<>(
                eventAdminService.update(eventId, body),
                HttpStatus.OK
        );
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
