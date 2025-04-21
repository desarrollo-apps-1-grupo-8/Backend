package ar.edu.uade.desa1.exception;

public class NotActiveUserException extends RuntimeException {
    public NotActiveUserException(String message) {
        super(message);
    }
} 