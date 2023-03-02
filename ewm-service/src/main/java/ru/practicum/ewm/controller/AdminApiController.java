package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.ewm.api.AdminApi;
import ru.practicum.ewm.api.dto.*;
import ru.practicum.ewm.api.dto.validation.*;
import ru.practicum.ewm.category.service.AdminCategoryService;
import ru.practicum.ewm.compilation.service.AdminCompilationService;
import ru.practicum.ewm.event.service.AdminEventService;
import ru.practicum.ewm.user.service.AdminUserService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminApiController implements AdminApi {

    private final AdminUserService adminUserService;
    private final AdminCategoryService adminCategoryService;
    private final AdminEventService adminEventService;
    private final AdminCompilationService adminCompilationService;

    private final NewCategoryDtoValidator newCategoryDtoValidator;
    private final UpdateCategoryDtoValidator updateCategoryDtoValidator;
    private final NewUserRequestValidator NewUserRequestValidator;
    private final UpdateEventAdminRequestValidator UpdateEventAdminRequestValidator;
    private final StringDateTimeValidator stringDateTimeValidator;
    private final StringStateEnumValidator stringStateEnumValidator;
    private final RandomAccessPageRequestValidator randomAccessPageRequestValidator;

    @Override
    public ResponseEntity<CategoryDto> adminAddCategory(NewCategoryDto body) {
        newCategoryDtoValidator.requireValid(body);

        return new ResponseEntity<>(
                adminCategoryService.add(body),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<Void> adminRemoveCategory(Long catId) {
        adminCategoryService.remove(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<CategoryDto> adminUpdateCategory(Long catId, CategoryDto body) {
        updateCategoryDtoValidator.requireValid(body);

        return new ResponseEntity<>(
                adminCategoryService.update(catId, body),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<UserDto> adminAddUser(NewUserRequest body) {
        NewUserRequestValidator.requireValid(body);

        return new ResponseEntity<>(
                adminUserService.add(body),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<Void> adminRemoveUser(Long userId) {
        adminUserService.remove(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<UserDto>> adminGetUsers(List<Long> ids, Integer from, Integer size) {
        return new ResponseEntity<>(
                adminUserService.getByIds(ids, from, size),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<EventFullDto>> adminFindEvents(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size
    ) {
        List<EventFullDto.StateEnum> filterStates = stringStateEnumValidator.requireValidOrNull(states, "states");
        LocalDateTime filterStart = stringDateTimeValidator.requireValidOrNull(rangeStart, "rangeStart");
        LocalDateTime filterEnd = stringDateTimeValidator.requireValidOrNull(rangeEnd, "rangeEnd");
        randomAccessPageRequestValidator.requireValid(from, size);

        return new ResponseEntity<>(
                adminEventService.find(
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
    public ResponseEntity<EventFullDto> adminUpdateEvent(Long eventId, UpdateEventAdminRequest body) {
        UpdateEventAdminRequestValidator.requireValid(body);

        return new ResponseEntity<>(
                adminEventService.update(eventId, body),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<CompilationDto> adminAddCompilation(NewCompilationDto body) {
        return new ResponseEntity<>(
                adminCompilationService.add(body),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<CompilationDto> adminUpdateCompilation(Long compId, UpdateCompilationRequest body) {
        return new ResponseEntity<>(
                adminCompilationService.update(compId, body),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<Void> adminRemoveCompilation(Long compId) {
        adminCompilationService.remove(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
