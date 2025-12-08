package org.example.kursach.service;

import org.example.kursach.domain.SystemUser;
import org.example.kursach.domain.UserRole;
import org.example.kursach.dto.AuthRegisterRequest;
import org.example.kursach.dto.AuthRequest;
import org.example.kursach.dto.AuthResponse;
import org.example.kursach.dto.SystemUserDto;
import org.example.kursach.exception.BusinessRuleException;
import org.example.kursach.exception.NotFoundException;
import org.example.kursach.mapper.MasterDataMapper;
import org.example.kursach.repository.SystemUserRepository;
import org.example.kursach.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;

@Service
public class AuthService {

    private final SystemUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(SystemUserRepository repository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(AuthRegisterRequest request) {
        if (repository.existsByUsername(request.username())) {
            throw new BusinessRuleException("Username already exists");
        }
        SystemUser user = new SystemUser();
        user.setUsername(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setRoles(EnumSet.of(UserRole.BUSINESS_USER));
        SystemUser saved = repository.save(user);
        String token = jwtService.generateToken(buildUserDetails(saved));
        return new AuthResponse(token, saved.getUsername());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        UserDetails userDetails = repository.findByUsername(request.username())
                .map(this::buildUserDetails)
                .orElseThrow(() -> new NotFoundException("User %s not found".formatted(request.username())));
        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token, userDetails.getUsername());
    }

    private UserDetails buildUserDetails(SystemUser user) {
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(user.getRoles().stream()
                        .map(r -> "ROLE_" + r.name())
                        .toList())
                .accountExpired(false)
                .accountLocked(!user.isActive())
                .credentialsExpired(false)
                .disabled(!user.isActive())
                .build();
    }

    @Transactional(readOnly = true)
    public SystemUserDto me(String username) {
        return repository.findByUsername(username)
                .map(MasterDataMapper::toDto)
                .orElseThrow(() -> new NotFoundException("User %s not found".formatted(username)));
    }
}

