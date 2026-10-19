package org.scaler.ordermanagementservice.services;

import org.scaler.ordermanagementservice.dtos.CreateUserRequestDto;
import org.scaler.ordermanagementservice.dtos.UserResponseDto;
import org.scaler.ordermanagementservice.models.User;
import org.scaler.ordermanagementservice.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto createUser(CreateUserRequestDto request) {
        String username = request.username().trim();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        List<String> roles = request.roles().stream()
                .map(role -> role.trim().toUpperCase(Locale.ROOT))
                .distinct()
                .toList();

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        return new UserResponseDto(savedUser.getId(), savedUser.getUsername(), savedUser.getRoles());
    }
}
