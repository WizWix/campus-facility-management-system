package io.github.wizwix.cfms.dto.response.cafeteria;

/// 할인 정보 (학식/푸드코트 공통)
///
/// @param price 할인가
/// @param label 할인 사유 (e.g. '학생 할인')
public record ResponseDiscount(int price, String label) {}
