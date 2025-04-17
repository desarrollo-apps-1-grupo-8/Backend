package ar.edu.uade.desa1.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("Usuario no encontrado con email: " + email);
    }
} 