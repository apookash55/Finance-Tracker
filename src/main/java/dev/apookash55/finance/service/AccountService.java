package dev.apookash55.finance.service;

import dev.apookash55.finance.dto.AccountInfo;
import dev.apookash55.finance.dto.AccountType;
import dev.apookash55.finance.entity.Account;
import dev.apookash55.finance.entity.User;
import dev.apookash55.finance.repository.AccountRepository;
import dev.apookash55.finance.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final CredentialRepository credentialRepository;

    public void createAccount(AccountInfo request, String username) {
        User user = getUser(username);

        Account account = new Account();
        account.setUser(user);
        account.setName(request.getName());
        account.setBalance(BigDecimal.valueOf(request.getBalance()));
        account.setType(String.valueOf(request.getType()));
        accountRepository.save(account);
    }

    public List<AccountInfo> getAccounts(String username) {
        User user = getUser(username);

        List<Account> accounts = accountRepository.findByUser(user);
        return accounts.stream().map(account -> new AccountInfo(account.getId(), account.getName(), AccountType.valueOf(account.getType()), account.getCurrency(), account.getBalance().doubleValue())).toList();
    }

    public AccountInfo getAccount(Long accountId, String username) {
        Account account = verifyAccount(accountId, username);
        return new AccountInfo(account.getId(), account.getName(), AccountType.valueOf(account.getType()), account.getCurrency(), account.getBalance().doubleValue());
    }

    public void updateAccount(Long accountId, AccountInfo request, String username) {
        Account account = verifyAccount(accountId, username);
        account.setName(request.getName());
        account.setBalance(BigDecimal.valueOf(request.getBalance()));
        account.setType(String.valueOf(request.getType()));
        accountRepository.save(account);
    }

    public void deleteAccount(Long accountId, String username) {
        Account account = verifyAccount(accountId, username);
        accountRepository.delete(account);
    }

    private User getUser(String username) {
        return credentialRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username")).getUser();
    }

    private Account verifyAccount(Long accountId, String username) {
        User user = credentialRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username")).getUser();
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new UsernameNotFoundException("Invalid account"));
        if (!account.getUser().equals(user)) {
            throw new IllegalArgumentException("Invalid account-user mapping");
        }
        return account;
    }

}
