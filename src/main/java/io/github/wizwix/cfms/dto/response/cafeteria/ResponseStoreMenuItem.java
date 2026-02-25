package io.github.wizwix.cfms.dto.response.cafeteria;

/// 푸드코트 메뉴 항목
///
/// @param name     메뉴명
/// @param price    정가
/// @param discount 할인 정보 (없으면 null)
/// @param popular  인기 메뉴 여부
public record ResponseStoreMenuItem(String name, int price, ResponseDiscount discount, boolean popular) {}
