package dev.apookash55.finance.service;

import dev.apookash55.finance.dto.TransactionInfo;
import dev.apookash55.finance.entity.Account;
import dev.apookash55.finance.entity.Category;
import dev.apookash55.finance.entity.Transaction;
import dev.apookash55.finance.entity.User;
import dev.apookash55.finance.repository.AccountRepository;
import dev.apookash55.finance.repository.CategoryRepository;
import dev.apookash55.finance.repository.CredentialRepository;
import dev.apookash55.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final CredentialRepository credentialRepository;

    @Transactional
    public void createTransaction(TransactionInfo request, String username) {
        User user = getUser(username);

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("Invalid category id"));
        Account account = accountRepository.findById(request.getAccountId()).orElseThrow(() -> new IllegalArgumentException("Invalid account id"));

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setAccount(account);
        transaction.setAmount(BigDecimal.valueOf(request.getAmount()));
        transaction.setTxnDate(LocalDateTime.parse(request.getTransactionDate()));
        transaction.setDescription(request.getDescription());
        transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionInfo> getTransactions(String username) {
        User user = getUser(username);

        List<Transaction> transactions = user.getTransactions();
        return transactions.stream().map(transaction -> new TransactionInfo(transaction.getId(), transaction.getCategory().getId(), transaction.getAccount().getId(), transaction.getAmount().doubleValue(), transaction.getTxnDate().toString(), transaction.getDescription())).toList();
    }

    @Transactional(readOnly = true)
    public TransactionInfo getTransaction(Long id, String username) {
        Transaction transaction = verifyTransaction(id, username);
        return new TransactionInfo(transaction.getId(), transaction.getCategory().getId(), transaction.getAccount().getId(), transaction.getAmount().doubleValue(), transaction.getTxnDate().toString(), transaction.getDescription());
    }

    @Transactional
    public void updateTransaction(TransactionInfo request, Long id, String username) {
        Transaction transaction = verifyTransaction(id, username);

        transaction.setCategory(categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("Invalid category id")));
        transaction.setAccount(accountRepository.findById(request.getAccountId()).orElseThrow(() -> new IllegalArgumentException("Invalid account id")));
        transaction.setAmount(BigDecimal.valueOf(request.getAmount()));
        transaction.setTxnDate(LocalDateTime.parse(request.getTransactionDate()));
        transaction.setDescription(request.getDescription());
        transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id, String username) {
        Transaction transaction = verifyTransaction(id, username);

        transactionRepository.delete(transaction);
    }

    private User getUser(String username) {
        return credentialRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username")).getUser();
    }

    private Transaction verifyTransaction(Long id, String username) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid transaction id"));
        User user = getUser(username);
        if (!transaction.getUser().equals(user)) {
            throw new IllegalArgumentException("Invalid transaction");
        }
        return transaction;
    }
}
