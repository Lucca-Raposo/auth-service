package com.raposo.auth_service.services;

import com.raposo.auth_service.models.dto.LoginRequestDTO;
import com.raposo.auth_service.models.user.User;
import com.raposo.auth_service.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public void verifyCredentials(LoginRequestDTO loginRequest){

        var user = userRepository.findByUsername(loginRequest.username());

        if(user.isEmpty() || !passwordEncoder.matches(loginRequest.password(), user.get().getPassword())){
            throw new BadCredentialsException("User or password is invalid!");
        }
    }

    public JwtClaimsSet buildClaims(User user){
        return JwtClaimsSet.builder()
                .issuer("auth-service")
                .subject(user.getId().toString())
                .expiresAt(Instant.now().plusSeconds(1800L))
                .issuedAt(Instant.now())
                .build();
    }

}
