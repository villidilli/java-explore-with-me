package ru.practicum.exception;

public class FieldConflictException extends RuntimeException {

    public FieldConflictException(String message) {
        super(message);
    }
}