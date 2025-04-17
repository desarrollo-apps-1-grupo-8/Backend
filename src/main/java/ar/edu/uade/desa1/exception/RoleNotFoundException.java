package ar.edu.uade.desa1.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(Long roleId) {
        super("Rol no encontrado con id: " + roleId);
    }
} 