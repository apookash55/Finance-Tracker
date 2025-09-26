package dev.apookash55.finance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionInfo {
    Long id;

    @NotNull
    private Long categoryId;

    @NotNull
    private Long accountId;

    @NotNull
    private Double amount;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime transactionDate;

    private String description;
}
