package io.github.wizwix.cfms.model;

import io.github.wizwix.cfms.model.enums.MealType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "cfms_cafeteria_meal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CafeteriaMeal {
  /// 끼니 ID
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /// 날짜
  private LocalDate date;
  /// 끼니 유형 (BREAKFAST/LUNCH/DINNER)
  @Enumerated(EnumType.STRING)
  private MealType mealType;
  /// 시간대 (e.g. '08:00 ~ 09:30')
  private String time;
  /// 아이콘 (e.g. '🌅')
  private String icon;
}
