package dev.apookash55.finance.service;

import dev.apookash55.finance.dto.UserInfoResponse;
import dev.apookash55.finance.entity.Credential;
import dev.apookash55.finance.entity.User;
import dev.apookash55.finance.repository.CredentialRepository;
import dev.apookash55.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CredentialRepository credentialRepository;
    private static final String INVALID_USERNAME = "Invalid username";

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(String username) {
        Credential credential = credentialRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(INVALID_USERNAME));
        return new UserInfoResponse(credential.getUser().getFirstName(), credential.getUser().getLastName(), credential.getUser().getEmail(), credential.getUsername());
    }

    @Transactional
    public void updateUser(String username, UserInfoResponse userInfo) {
        Credential credential = credentialRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(INVALID_USERNAME));
        User user = credential.getUser();
        user.setFirstName(userInfo.getFirstName());
        user.setLastName(userInfo.getLastName());
        user.setEmail(userInfo.getEmail());
        userRepository.save(user);
        credential.setUsername(userInfo.getUsername());
    }

    @Transactional
    public void deleteUser(String username) {
        Credential credential = credentialRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(INVALID_USERNAME));
        credentialRepository.delete(credential);
        userRepository.delete(credential.getUser());
    }
}
