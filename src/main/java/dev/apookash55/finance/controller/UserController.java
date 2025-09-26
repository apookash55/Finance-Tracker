package dev.apookash55.finance.controller;

import dev.apookash55.finance.dto.UserInfoResponse;
import dev.apookash55.finance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMe(Authentication authentication) {
        String username = authentication.getName();
        UserInfoResponse userInfo = userService.getUserInfo(username);
        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateUser(Authentication authentication, @RequestBody UserInfoResponse userInfo) {
        String username = authentication.getName();
        userService.updateUser(username, userInfo);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        String username = authentication.getName();
        userService.deleteUser(username);
        return ResponseEntity.ok(null);
    }
}
