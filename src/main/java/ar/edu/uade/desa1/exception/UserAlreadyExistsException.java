package ar.edu.uade.desa1.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("Usuario con email " + email + " ya existe");
    }
} 