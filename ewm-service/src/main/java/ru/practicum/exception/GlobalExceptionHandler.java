package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice("ru.practicum")
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError exceptionHandler(ConstraintViolationException e) {
        log.debug("/conflict handler");
        return prepareErrorResponse(HttpStatus.CONFLICT,
                            "Data integrity violation",
                                    e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError exceptionHandler(MethodArgumentNotValidException e) {
        log.debug("/conflict handler");
        return prepareErrorResponse(HttpStatus.BAD_REQUEST,
                            "Incorrectly request input",
                                    e.getBindingResult().toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError exceptionHandler(NotFoundException e) {
        log.debug("/not found handler");
        ApiError error
    }

    private ApiError prepareErrorResponse(HttpStatus status, String reason, String message) {
        ApiError error = new ApiError();
        error.setStatus(status.name());
        error.setReason(reason);
        error.setMessage(message);
        error.setTimestamp(LocalDateTime.now());
    }
}