package io.github.wizwix.cfms.dto.response.auth;

import java.time.LocalDateTime;

public record ResponseUserProfile(Long id, String name, String userNumber, String email, LocalDateTime createdAt) {}
