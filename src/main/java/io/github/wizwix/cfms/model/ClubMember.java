package io.github.wizwix.cfms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cfms_club_member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubMember {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // 순수 인덱싱용
  @ManyToOne
  private Club club;
  private LocalDateTime joinedAt;
  @Enumerated(EnumType.STRING)
  private ClubRole role;
  @Enumerated(EnumType.STRING)
  private EntryStatus status;
  @ManyToOne
  private User user;
}
