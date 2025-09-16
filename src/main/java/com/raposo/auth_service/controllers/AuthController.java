package com.raposo.auth_service.controllers;

import com.raposo.auth_service.models.dto.AuthTokensDTO;
import com.raposo.auth_service.models.dto.LoginRequestDTO;
import com.raposo.auth_service.models.dto.LoginResponseDTO;
import com.raposo.auth_service.models.token.RefreshToken;
import com.raposo.auth_service.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest, HttpServletResponse response){
        try{
            AuthTokensDTO authTokens = authService.generateAcessToken(loginRequest);

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", authTokens.refreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("/auth/refresh")
                    .maxAge(24 * 60 * 60)
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

            LoginResponseDTO loginResponse = new LoginResponseDTO(
                    authTokens.accessToken(),
                    authTokens.expiresIn()
            );

            return ResponseEntity.ok(loginResponse);

        } catch (BadCredentialsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
