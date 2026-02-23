package io.github.wizwix.cfms.dto.response.cafeteria;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record ResponseCafeteria(LocalDate date, Map<String, List<String>> menuByCategory) {}
