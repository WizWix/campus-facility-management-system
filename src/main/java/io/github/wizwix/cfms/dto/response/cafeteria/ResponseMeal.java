package io.github.wizwix.cfms.dto.response.cafeteria;

import java.util.List;

/// 학식 한 끼 (조식/중식/석식)
///
/// @param type  끼니 유형 (조식/중식/석식)
/// @param time  시간대 (e.g. '08:00 ~ 09:30')
/// @param icon  아이콘 (e.g. '🌅')
/// @param items 메뉴 항목 목록
public record ResponseMeal(String type, String time, String icon, List<ResponseMealItem> items) {}
