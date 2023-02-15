package ru.doggo.exceptions;

public class DoggoException extends RuntimeException {

    public DoggoException(String message) {
        super(message);
    }

    public enum ExceptionType {
        S3_KEY_DOES_NOT_EXIST
    }
}
