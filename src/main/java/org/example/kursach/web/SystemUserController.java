package org.example.kursach.web;

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
public class SystemUserController {

    private final UserService userService;

    public SystemUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<SystemUserDto> list() {
        return userService.all();
    }

    @GetMapping("/{id}")
    public SystemUserDto get(@PathVariable Long id) {
        return userService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SystemUserDto create(@Valid @RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    @PutMapping("/{id}")
    public SystemUserDto update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return userService.update(id, request);
    }

    @PostMapping("/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@PathVariable Long id, @Valid @RequestBody PasswordChangeRequest request) {
        userService.changePassword(id, request);
    }
}

