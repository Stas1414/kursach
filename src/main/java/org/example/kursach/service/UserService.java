package org.example.kursach.service;

import org.example.kursach.domain.SystemUser;
import org.example.kursach.domain.UserRole;
import org.example.kursach.dto.CreateUserRequest;
import org.example.kursach.dto.PasswordChangeRequest;
import org.example.kursach.dto.SystemUserDto;
import org.example.kursach.dto.UpdateUserRequest;
import org.example.kursach.exception.BusinessRuleException;
import org.example.kursach.exception.NotFoundException;
import org.example.kursach.mapper.MasterDataMapper;
import org.example.kursach.repository.SystemUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final SystemUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(SystemUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<SystemUserDto> all() {
        return repository.findAll().stream()
                .map(MasterDataMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public SystemUserDto get(Long id) {
        return repository.findById(id)
                .map(MasterDataMapper::toDto)
                .orElseThrow(() -> new NotFoundException("User %d not found".formatted(id)));
    }

    @Transactional
    public SystemUserDto create(CreateUserRequest request) {
        if (repository.existsByUsername(request.username())) {
            throw new BusinessRuleException("Username already exists");
        }
        SystemUser user = new SystemUser();
        user.setUsername(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setRoles(resolveRoles(request.roles()));
        return MasterDataMapper.toDto(repository.save(user));
    }

    @Transactional
    public SystemUserDto update(Long id, UpdateUserRequest request) {
        SystemUser user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User %d not found".formatted(id)));
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setRoles(resolveRoles(request.roles()));
        user.setActive(request.active());
        return MasterDataMapper.toDto(user);
    }

    @Transactional
    public void changePassword(Long id, PasswordChangeRequest request) {
        SystemUser user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User %d not found".formatted(id)));
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    private Set<UserRole> resolveRoles(Set<UserRole> roles) {
        if (roles == null || roles.isEmpty()) {
            return EnumSet.of(UserRole.BUSINESS_USER);
        }
        return EnumSet.copyOf(roles);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemUser user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));
        if (!user.isActive()) {
            throw new UsernameNotFoundException("User %s disabled".formatted(username));
        }
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
        return new User(user.getUsername(), user.getPasswordHash(), authorities);
    }
}

