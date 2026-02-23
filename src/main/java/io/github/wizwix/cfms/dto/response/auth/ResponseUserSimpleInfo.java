package io.github.wizwix.cfms.dto.response.auth;

import io.github.wizwix.cfms.model.enums.UserRole;

public record ResponseUserSimpleInfo(Long id, String name, String userNumber, UserRole role) {}
