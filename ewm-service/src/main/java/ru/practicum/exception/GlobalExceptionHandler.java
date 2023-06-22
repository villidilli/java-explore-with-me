package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice("ru.practicum")
@Slf4j
public class GlobalExceptionHandler {

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.CONFLICT)
//    public ApiError exceptionHandler(ConstraintViolationException e) {
//        log.debug("/conflict handler");
//        return prepareErrorResponse(HttpStatus.CONFLICT,
//                            "Conflict fields",
//                                    e.getLocalizedMessage());
//    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError exceptionHandler(FieldConflictException e) {
        log.debug("/conflict handler FieldConflictException");
        return prepareErrorResponse(HttpStatus.CONFLICT,
                "Conflict fields",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError exceptionHandler(ValidateException e) {
        log.debug("/conflict handler ValidateException");
        return prepareErrorResponse(HttpStatus.BAD_REQUEST,
                "Field validation failed",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError exceptionHandler(DataIntegrityViolationException e) {
        log.debug("/conflict handler DataIntegrityViolationException");
        return prepareErrorResponse(HttpStatus.CONFLICT,
                "Conflict fields",
                e.getRootCause().getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError exceptionHandler(MethodArgumentNotValidException e) {
        log.debug("/conflict handler MethodArgumentNotValidException");
        return prepareErrorResponse(HttpStatus.BAD_REQUEST,
                            "Incorrectly request input",
                                    e.getBindingResult().toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError exceptionHandler(NotFoundException e) {
        log.debug("/not found handler");
        return prepareErrorResponse(HttpStatus.NOT_FOUND,
                                    "Object not found",
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