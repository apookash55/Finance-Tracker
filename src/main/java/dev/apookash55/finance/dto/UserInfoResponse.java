package dev.apookash55.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
}
