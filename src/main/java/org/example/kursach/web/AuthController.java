package org.example.kursach.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.kursach.dto.AuthRegisterRequest;
import org.example.kursach.dto.AuthRequest;
import org.example.kursach.dto.AuthResponse;
import org.example.kursach.dto.SystemUserDto;
import org.example.kursach.service.AuthService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Регистрация, вход и получение профиля текущего пользователя.")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация", description = "Создаёт нового пользователя (роль BUSINESS_USER) и выдаёт JWT.")
    public AuthResponse register(@Valid @RequestBody AuthRegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Логин", description = "Проверяет креды и выдаёт JWT.")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    @Operation(summary = "Текущий пользователь", description = "Возвращает профиль текущего пользователя по JWT.")
    public SystemUserDto me(@AuthenticationPrincipal UserDetails userDetails) {
        return authService.me(userDetails.getUsername());
    }
}

