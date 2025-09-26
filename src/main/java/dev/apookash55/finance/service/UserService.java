package dev.apookash55.finance.service;

import dev.apookash55.finance.dto.UserInfoResponse;
import dev.apookash55.finance.entity.Credential;
import dev.apookash55.finance.entity.User;
import dev.apookash55.finance.repository.CredentialRepository;
import dev.apookash55.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CredentialRepository credentialRepository;
    private static final String INVALID_USERNAME = "Invalid username";

    public UserInfoResponse getUserInfo(String username) {
        Credential credential = credentialRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(INVALID_USERNAME));
        return new UserInfoResponse(credential.getUser().getFirstName(), credential.getUser().getLastName(), credential.getUser().getEmail(), credential.getUsername());
    }

    public void updateUser(String username, UserInfoResponse userInfo) {
        Credential credential = credentialRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(INVALID_USERNAME));
        User user = credential.getUser();
        user.setFirstName(userInfo.getFirstName());
        user.setLastName(userInfo.getLastName());
        user.setEmail(userInfo.getEmail());
        userRepository.save(user);
        credential.setUsername(userInfo.getUsername());
    }

    public void deleteUser(String username) {
        Credential credential = credentialRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(INVALID_USERNAME));
        User user = credential.getUser();
        userRepository.delete(user);
    }
}
