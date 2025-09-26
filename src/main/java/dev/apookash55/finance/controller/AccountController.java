package dev.apookash55.finance.controller;

import dev.apookash55.finance.dto.AccountInfo;
import dev.apookash55.finance.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public void createAccount(@Valid @RequestBody AccountInfo request, Authentication authentication) {
        String username = authentication.getName();
        accountService.createAccount(request, username);
    }

    @GetMapping
    public ResponseEntity<List<AccountInfo>> getAccounts(Authentication authentication) {
        String username = authentication.getName();
        List<AccountInfo> accounts = accountService.getAccounts(username);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountInfo> getAccount(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        AccountInfo account = accountService.getAccount(id, username);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{id}")
    public void updateAccount(@Valid @RequestBody AccountInfo request, @PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        accountService.updateAccount(id, request, username);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        accountService.deleteAccount(id, username);
    }
}
