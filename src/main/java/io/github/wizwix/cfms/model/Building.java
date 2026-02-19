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
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id; // 순수 인덱싱용
  private String name;
  private boolean rentable;
}
