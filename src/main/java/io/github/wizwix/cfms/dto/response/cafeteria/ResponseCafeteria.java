package io.github.wizwix.cfms.dto.response.cafeteria;

import java.time.LocalDate;
import java.util.List;

/// 오늘의 학식 전체 응답 — 프론트 TODAY_MEALS 구조 대응
///
/// @param date  날짜
/// @param meals 끼니 목록 (조식/중식/석식)
public record ResponseCafeteria(LocalDate date, List<ResponseMeal> meals) {}
