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
import ru.practicum.ewm.errors.dto.ApiErrorDto;
import ru.practicum.ewm.exceptions.NotFoundException;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({
            NotFoundException.class
    })
    public ResponseEntity<ApiErrorDto> handleNotFoundException(Throwable throwable) {
        return createResponseEntity(throwable, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            DataIntegrityViolationException.class
    })
    public ResponseEntity<ApiErrorDto> handleConflictException(Throwable throwable) {
        return createResponseEntity(throwable, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            ValidationException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ApiErrorDto> handleValidationException(Throwable throwable) {
        return createResponseEntity(throwable, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorDto> handleOtherwise(Throwable throwable) {
        log.error("Unexpected error occurred:\n{}", ExceptionUtils.getStackTrace(throwable));
        return createResponseEntity(throwable, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiErrorDto> createResponseEntity(
            Throwable throwable,
            HttpStatus status
    ) {
        //org.apache.commons.
        return new ResponseEntity<>(
                new ApiErrorDto(
                        status,
                        status.getReasonPhrase(),
                        throwable.getMessage(),
                        null,
                        LocalDateTime.now()
                ),
                status
        );
    }
}