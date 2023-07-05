package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice("ru.practicum")
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError exceptionHandler(FieldConflictException e) {
        log.debug("/conflict handler FieldConflictException");
        return prepareErrorResponse(HttpStatus.CONFLICT,
                "For the requested operation the conditions are not met.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError exceptionHandler(ValidateException e) {
        log.debug("/conflict handler ValidateException");
        return prepareErrorResponse(HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError exceptionHandler(DataIntegrityViolationException e) {
        log.debug("/conflict handler DataIntegrityViolationException");
        return prepareErrorResponse(HttpStatus.CONFLICT,
                "For the requested operation the conditions are not met.",
                e.getRootCause().getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError exceptionHandler(MissingServletRequestParameterException e) {
        log.debug("/missing required parameter handler");
        return prepareErrorResponse(HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError exceptionHandler(MethodArgumentNotValidException e) {
        log.debug("/conflict handler MethodArgumentNotValidException");
        return prepareErrorResponse(HttpStatus.BAD_REQUEST,
            "Incorrectly made request.",
            "Field: " + e.getFieldError().getField() + ". Error: " + e.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError exceptionHandler(NotFoundException e) {
        log.debug("/not found handler");
        return prepareErrorResponse(HttpStatus.NOT_FOUND,
                                    "The required object was not found.",
                                    e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError exceptionHandler(Throwable e) {
        log.debug("/server error handler");
        return prepareErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                e.getMessage());
    }

    private ApiError prepareErrorResponse(HttpStatus status, String reason, String message) {
        ApiError error = new ApiError();
        error.setStatus(status.name());
        error.setReason(reason);
        error.setMessage(message);
        error.setTimestamp(LocalDateTime.now());
        return error;
    }
}