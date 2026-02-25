package io.github.wizwix.cfms.dto.response.cafeteria;

/// 학식 메뉴 항목
///
/// @param name     메뉴명
/// @param price    정가
/// @param discount 할인 정보 (없으면 null)
public record ResponseMealItem(String name, int price, ResponseDiscount discount) {}
