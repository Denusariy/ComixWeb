package ru.denusariy.Comix.exception;

public class ComicNotFoundException extends RuntimeException {
    public ComicNotFoundException(String message) {
        super(message);
    }
}
