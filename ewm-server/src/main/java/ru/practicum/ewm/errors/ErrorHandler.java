package ru.practicum.ewm.errors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.api.model.ApiError;
import ru.practicum.ewm.exceptions.NotFoundException;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({
            NotFoundException.class
    })
    public ResponseEntity<ApiError> handleNotFoundException(Throwable throwable) {
        return createResponseEntity(throwable, ErrorStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            DataIntegrityViolationException.class
    })
    public ResponseEntity<ApiError> handleConflictException(Throwable throwable) {
        return createResponseEntity(throwable, ErrorStatus.CONFLICT);
    }

    @ExceptionHandler({
            ValidationException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ApiError> handleValidationException(Throwable throwable) {
        return createResponseEntity(throwable, ErrorStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiError> handleOtherwise(Throwable throwable) {
        log.error("Unexpected error occurred:\n{}", ExceptionUtils.getStackTrace(throwable));
        return createResponseEntity(throwable, ErrorStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiError> createResponseEntity(
            Throwable throwable,
            ErrorStatus info
    ) {
        //org.apache.commons.
        return new ResponseEntity<>(
                new ApiError()
                        .status(info.getApiStatus())
                        .reason(info.getReason())
                        .message(throwable.getMessage())
                        .errors(List.of(throwable.getStackTrace()).stream().map(Object::toString).collect(Collectors.toList()))
                        .timestamp(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())),
                info.getHttpStatus()
        );
    }
}
