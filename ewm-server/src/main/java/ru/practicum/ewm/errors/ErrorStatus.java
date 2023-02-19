package ru.practicum.ewm.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.practicum.ewm.api.model.ApiError;

@Getter
@RequiredArgsConstructor
enum ErrorStatus {
    NOT_FOUND(
            HttpStatus.NOT_FOUND,
            ApiError.StatusEnum._404_NOT_FOUND,
            "Unable to find the resource requested"
    ),
    CONFLICT(
            HttpStatus.CONFLICT,
            ApiError.StatusEnum._409_CONFLICT,
            "The request conflicts with the current state of the server"
    ),
    BAD_REQUEST(
            HttpStatus.BAD_REQUEST,
            ApiError.StatusEnum._400_BAD_REQUEST,
            "The request is malformed"
    ),
    INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ApiError.StatusEnum._500_INTERNAL_SERVER_ERROR,
            "Internal server error occurred"
    );

    private final HttpStatus httpStatus;
    private final ApiError.StatusEnum apiStatus;
    private final String reason;
}
