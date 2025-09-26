package dev.apookash55.finance.service;

import dev.apookash55.finance.dto.LoginUserRequest;
import dev.apookash55.finance.dto.LoginUserResponse;
import dev.apookash55.finance.dto.RegisterUserRequest;
import dev.apookash55.finance.entity.Credential;
import dev.apookash55.finance.entity.User;
import dev.apookash55.finance.repository.CredentialRepository;
import dev.apookash55.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(RegisterUserRequest request) {
       User user = new User();
       user.setFirstName(request.getFirstName());
       user.setLastName(request.getLastName());
       user.setEmail(request.getEmail());
       userRepository.save(user);

       Credential credential = new Credential();
       credential.setUsername(request.getUsername());
       credential.setPasswordHash(passwordEncoder.encode(request.getPassword()));
       credential.setUser(user);
       credentialRepository.save(credential);
    }

    @Transactional
    public LoginUserResponse loginUser(LoginUserRequest request) {
        Credential credential = credentialRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

        if(!passwordEncoder.matches(request.getPassword(), credential.getPasswordHash())) {
            throw new UsernameNotFoundException("Invalid username or password");
        }

        credential.setLastLogin(LocalDateTime.now());
        credentialRepository.save(credential);

        String token = jwtService.generateToken(credential.getUsername());

        return new LoginUserResponse(token, credential.getUsername(), credential.getLastLogin());
    }
}
