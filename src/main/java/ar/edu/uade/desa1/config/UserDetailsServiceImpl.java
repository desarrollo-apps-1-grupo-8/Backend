package ar.edu.uade.desa1.config;

import ar.edu.uade.desa1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//Este es el punto entre el token y la base de datos, esta clase sirve para cargar el usuario autenticado a la DB.
//JWT da el email dentro del token, pero hay que decirle a Spring cómo conseguir el UserDetails completo (con roles, permisos, etc).
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
