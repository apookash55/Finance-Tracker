package dev.apookash55.finance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountInfo {
    Long id;

    @NotBlank
    private String name;

    @NotNull
    private AccountType type;

    private String currency;

    @NotNull
    private Double balance;
}
