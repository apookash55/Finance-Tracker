package dev.apookash55.finance.service;

import dev.apookash55.finance.dto.CategoryType;
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
        transaction.setTxnDate(request.getTransactionDate());
        transaction.setDescription(request.getDescription());
        transactionRepository.save(transaction);

        if (category.getType().equals(CategoryType.EXPENSE.name())) {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        }
        else {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        }
        accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public List<TransactionInfo> getTransactions(Long accountId, Long categoryId, String username) {
        User user = getUser(username);

        if (accountId != null && categoryId != null) {
            user.getAccounts().stream().filter(account -> account.getId().equals(accountId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid account id"));
            user.getCategories().stream().filter(category -> category.getId().equals(categoryId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid category id"));

            List<Transaction> transactions = user.getTransactions().stream().filter(transaction -> transaction.getAccount().getId().equals(accountId) && transaction.getCategory().getId().equals(categoryId)).toList();
            return transactions.stream().map(transaction -> new TransactionInfo(transaction.getId(), transaction.getCategory().getId(), transaction.getAccount().getId(), transaction.getAmount().doubleValue(), transaction.getTxnDate(), transaction.getDescription())).toList();
        }

        if (accountId != null) {
            user.getAccounts().stream().filter(account -> account.getId().equals(accountId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid account id"));

            List<Transaction> transactions = user.getTransactions().stream().filter(transaction -> transaction.getAccount().getId().equals(accountId)).toList();
            return transactions.stream().map(transaction -> new TransactionInfo(transaction.getId(), transaction.getCategory().getId(), transaction.getAccount().getId(), transaction.getAmount().doubleValue(), transaction.getTxnDate(), transaction.getDescription())).toList();
        }

        if (categoryId != null) {
            user.getCategories().stream().filter(category -> category.getId().equals(categoryId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid category id"));

            List<Transaction> transactions = user.getTransactions().stream().filter(transaction -> transaction.getCategory().getId().equals(categoryId)).toList();
            return transactions.stream().map(transaction -> new TransactionInfo(transaction.getId(), transaction.getCategory().getId(), transaction.getAccount().getId(), transaction.getAmount().doubleValue(), transaction.getTxnDate(), transaction.getDescription())).toList();
        }

        List<Transaction> transactions = user.getTransactions();
        return transactions.stream().map(transaction -> new TransactionInfo(transaction.getId(), transaction.getCategory().getId(), transaction.getAccount().getId(), transaction.getAmount().doubleValue(), transaction.getTxnDate(), transaction.getDescription())).toList();
    }

    @Transactional(readOnly = true)
    public TransactionInfo getTransaction(Long id, String username) {
        Transaction transaction = verifyTransaction(id, username);
        return new TransactionInfo(transaction.getId(), transaction.getCategory().getId(), transaction.getAccount().getId(), transaction.getAmount().doubleValue(), transaction.getTxnDate(), transaction.getDescription());
    }

    @Transactional
    public void updateTransaction(TransactionInfo request, Long id, String username) {
        Transaction transaction = verifyTransaction(id, username);
        BigDecimal oldAmount = transaction.getAmount();
        Category oldCategory = transaction.getCategory();
        Account oldAccount = transaction.getAccount();

        transaction.setCategory(categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("Invalid category id")));
        transaction.setAccount(accountRepository.findById(request.getAccountId()).orElseThrow(() -> new IllegalArgumentException("Invalid account id")));
        transaction.setAmount(BigDecimal.valueOf(request.getAmount()));
        transaction.setTxnDate(request.getTransactionDate());
        transaction.setDescription(request.getDescription());
        transactionRepository.save(transaction);

        if (oldCategory.getType().equals(CategoryType.EXPENSE.name())) {
            oldAccount.setBalance(oldAccount.getBalance().add(oldAmount));
        }
        else {
            oldAccount.setBalance(oldAccount.getBalance().subtract(oldAmount));
        }
        if (transaction.getCategory().getType().equals(CategoryType.EXPENSE.name())) {
            transaction.getAccount().setBalance(transaction.getAccount().getBalance().subtract(transaction.getAmount()));
        }
        else {
            transaction.getAccount().setBalance(transaction.getAccount().getBalance().add(transaction.getAmount()));
        }
        accountRepository.save(oldAccount);
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
