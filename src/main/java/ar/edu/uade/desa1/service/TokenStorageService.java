package ar.edu.uade.desa1.service;

import ar.edu.uade.desa1.domain.entity.User;
import ar.edu.uade.desa1.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenStorageService {

    private final UserRepository userRepository;
    private final Map<String, String> tokenStore = new HashMap<>();

    @Transactional
    public void saveToken(String userId, String token) {
        java.util.Optional<User> optionalUser = userRepository.findById(Long.parseLong(userId));
        if (optionalUser.isPresent()) {
            ar.edu.uade.desa1.domain.entity.User user = optionalUser.get();
            user.setPushToken(token);
            userRepository.save(user);
        }
    }


    public String getToken(String userId) {
        return userRepository.findById(Long.parseLong(userId))
                .map(User::getPushToken)
                .orElse(null);
    }

    public String getAnyAvailableDeliveryToken() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole().getId() == 1) // Rol = Repartidor
                .map(User::getPushToken)
                .filter(token -> token != null && !token.isEmpty())
                .findFirst()
                .orElse(null);
    }
}


