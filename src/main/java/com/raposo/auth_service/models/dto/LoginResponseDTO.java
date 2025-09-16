package com.raposo.auth_service.models.dto;

public record LoginResponseDTO(String accessToken, Long expiresIn) {
}
