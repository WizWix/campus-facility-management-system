package io.github.wizwix.cfms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cfms_building")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Building {
  /// 순수 인덱싱용
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  /// 건물 이름 (e.g. '강의동 1')
  private String name;
  /// 건물의 대여 가능한 방 존재 여부
  private boolean rentable;
}
