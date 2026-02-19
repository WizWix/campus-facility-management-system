package io.github.wizwix.cfms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "cfms_room",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"building_id", "room_number"}),
        @UniqueConstraint(columnNames = {"room_code"}),
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Room {
  // @Column은 생략 가능
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private Building building;
  @Column(unique = true, nullable = false)
  private String roomCode; // 'IT동 104호'
  private String roomNumber; // '104'
}
