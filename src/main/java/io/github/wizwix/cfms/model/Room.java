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
  /// 호실 ID: 순수 인덱싱용
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /// 호실이 속한 건물
  @ManyToOne(fetch = FetchType.LAZY)
  private Building building;
  /// 호실의 정식 명칭 (e.g. 'IT대학동 104호')
  @Column(unique = true, nullable = false)
  private String roomCode;
  /// 호실의 방 번호 (e.g. '104')
  private String roomNumber;
}
