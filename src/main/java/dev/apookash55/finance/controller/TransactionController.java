package dev.apookash55.finance.controller;

import dev.apookash55.finance.dto.TransactionInfo;
import dev.apookash55.finance.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public void createTransaction(@Valid @RequestBody TransactionInfo request, Authentication authentication) {
        String username = authentication.getName();
        transactionService.createTransaction(request, username);
    }

    @GetMapping
    public ResponseEntity<List<TransactionInfo>> getTransactions(@RequestParam(required = false) Long accountId,
                                                                 @RequestParam(required = false) Long categoryId,
                                                                 Authentication authentication) {
        String username = authentication.getName();
        List<TransactionInfo> transactions = transactionService.getTransactions(accountId, categoryId, username);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionInfo> getTransaction(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        TransactionInfo transaction = transactionService.getTransaction(id, username);
        return ResponseEntity.ok(transaction);
    }

    @PutMapping("/{id}")
    public void updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionInfo request, Authentication authentication) {
        String username = authentication.getName();
        transactionService.updateTransaction(request, id, username);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        transactionService.deleteTransaction(id, username);
    }
}
