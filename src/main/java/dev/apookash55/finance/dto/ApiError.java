package dev.apookash55.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {
    private String message;
    private String path;
    private LocalDateTime timestamp;
}
