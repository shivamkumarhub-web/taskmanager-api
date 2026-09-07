package com.shivam.taskmanager.exception;

/**
 * Thrown when attempting to register a user that already exists.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}