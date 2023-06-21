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
        ApiError error = new ApiError();
        error.setStatus(HttpStatus.CONFLICT.name());
        error.setReason("Data integrity violation");
        error.setMessage(e.getMessage());
        error.setTimestamp(LocalDateTime.now());
        return error;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError exceptionHandler(MethodArgumentNotValidException e) {
        log.debug("/conflict handler");
        ApiError error = new ApiError();
        error.setStatus(HttpStatus.CONFLICT.name());
        error.setReason("Incorrectly request input");
        error.setMessage(e.getBindingResult().toString());
        error.setTimestamp(LocalDateTime.now());
        return error;
    }
}