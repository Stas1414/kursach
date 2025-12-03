package org.example.kursach.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.kursach.dto.CreateUserRequest;
import org.example.kursach.dto.PasswordChangeRequest;
import org.example.kursach.dto.SystemUserDto;
import org.example.kursach.dto.UpdateUserRequest;
import org.example.kursach.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Управление пользователями системы и их ролями.")
public class SystemUserController {

    private final UserService userService;

    public SystemUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Список пользователей", description = "Возвращает всех пользователей системы.")
    public List<SystemUserDto> list() {
        return userService.all();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя", description = "Возвращает пользователя по идентификатору.")
    public SystemUserDto get(@PathVariable Long id) {
        return userService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать пользователя", description = "Создаёт нового пользователя и задаёт ему роли.")
    public SystemUserDto create(@Valid @RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя", description = "Обновляет ФИО, email, роли и статус активности пользователя.")
    public SystemUserDto update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return userService.update(id, request);
    }

    @PostMapping("/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Сменить пароль пользователя", description = "Изменяет пароль указанного пользователя.")
    public void changePassword(@PathVariable Long id, @Valid @RequestBody PasswordChangeRequest request) {
        userService.changePassword(id, request);
    }
}

