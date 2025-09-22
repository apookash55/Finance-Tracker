package dev.apookash55.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LoginUserResponse {
    private String token;
    private String username;
    private LocalDateTime lastLogin;
}
