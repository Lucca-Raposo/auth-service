package com.raposo.auth_service.services;

import com.raposo.auth_service.models.dto.AuthTokensDTO;
import com.raposo.auth_service.models.dto.LoginRequestDTO;
import com.raposo.auth_service.models.dto.LoginResponseDTO;
import com.raposo.auth_service.models.token.RefreshToken;
import com.raposo.auth_service.models.user.User;
import com.raposo.auth_service.repositories.RefreshTokenRepository;
import com.raposo.auth_service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Value("${jwt.expiresIn}")
    private Long expiresIn;

    public AuthTokensDTO generateAcessToken(LoginRequestDTO loginRequest){

        var user = userRepository.findByUsername(loginRequest.username());

        if(user.isEmpty() || !passwordEncoder.matches(loginRequest.password(), user.get().getPassword())){
            throw new BadCredentialsException("User or password is invalid!");
        }

        String jwtValue = buildJwt(user.get());

        RefreshToken refreshToken = generateRefreshToken(user.get());

        return new AuthTokensDTO(jwtValue, refreshToken.getToken(), expiresIn);
    }

    private String buildJwt(User user){

        var claims = JwtClaimsSet.builder()
                .issuer("auth-service")
                .subject(user.getId().toString())
                .expiresAt(Instant.now().plusSeconds(expiresIn))
                .issuedAt(Instant.now())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private RefreshToken generateRefreshToken(User user){
        RefreshToken refreshToken = new RefreshToken(user);
        return refreshTokenRepository.save(refreshToken);
    }

}
