package com.raposo.auth_service.services;

import com.raposo.auth_service.models.dto.RegisterRequestDTO;
import com.raposo.auth_service.models.dto.RegisterResponseDTO;
import com.raposo.auth_service.models.role.Role;
import com.raposo.auth_service.models.user.User;
import com.raposo.auth_service.repositories.RoleRepository;
import com.raposo.auth_service.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public RegisterResponseDTO registerUser(RegisterRequestDTO registerRequest){
        if(userRepository.findByUsername(registerRequest.username()).isPresent()) {
            throw new BadCredentialsException("Username already in use!");
        }
        else {
            User user = new User();
            user.setUsername(registerRequest.username());
            user.setPassword(passwordEncoder.encode(registerRequest.password()));
            user.setRole(roleRepository.findByName(Role.Values.BASIC.name()));

            return new RegisterResponseDTO(userRepository.save(user).getUsername());
        }
    }
}
