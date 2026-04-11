package com.stocs.inventorysystem.service;

import com.stocs.inventorysystem.model.AppUser;
import com.stocs.inventorysystem.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailExists(String email) {
        return appUserRepository.existsByEmail(email);
    }

    @Transactional
    public AppUser register(String email, String fullName, String rawPassword) {
        if (appUserRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
        AppUser user = AppUser.builder()
                .email(email)
                .fullName(fullName)
                .password(passwordEncoder.encode(rawPassword))
                .build();
        AppUser saved = appUserRepository.saveAndFlush(user);
        log.info("User registered: id={} email={}", saved.getId(), saved.getEmail());
        return saved;
    }
}
