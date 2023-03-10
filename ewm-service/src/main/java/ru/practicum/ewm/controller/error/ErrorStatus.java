package ru.practicum.ewm.controller.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.practicum.ewm.api.dto.ApiError;

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
    FORBIDDEN(
            HttpStatus.FORBIDDEN,
            ApiError.StatusEnum._403_FORBIDDEN,
            "For the requested operation the conditions are not met"
    ),
    NOT_IMPLEMENTED(
            HttpStatus.NOT_IMPLEMENTED,
            ApiError.StatusEnum._501_NOT_IMPLEMENTED,
            "Not Implemented"
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
