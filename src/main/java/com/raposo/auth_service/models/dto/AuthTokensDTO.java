package com.raposo.auth_service.models.dto;

public record AuthTokensDTO(String accessToken, String refreshToken, Long expiresIn) {
}
