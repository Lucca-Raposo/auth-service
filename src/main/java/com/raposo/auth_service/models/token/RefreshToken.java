package com.raposo.auth_service.models.token;

import com.raposo.auth_service.models.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "tb_refresh_token")
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;

    public RefreshToken(User user) {
        this.user = user;
        this.token = UUID.randomUUID().toString(); // Gera o token de verdade
        this.expiryDate = Instant.now().plus(1, ChronoUnit.DAYS);
    }
}
