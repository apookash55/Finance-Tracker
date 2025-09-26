package dev.apookash55.finance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
public class TransactionInfo {
    Long id;

    @NotNull
    private Long accountId;

    @NotNull
    private Long categoryId;

    @NotBlank
    private Double amount;

    private String description;

    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String transactionDate;
}
