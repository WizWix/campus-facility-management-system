package io.github.wizwix.cfms.model;

import jakarta.persistence.Column;
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
@Table(name = "cfms_club")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Club {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private boolean autoApprove;
  private LocalDateTime createdAt;
  @Column(length = 5000)
  private String description;
  private String name;
  @ManyToOne
  private User president; // 동아리장
  @Column(unique = true, length = 10)
  private String slug;
  @Enumerated(EnumType.STRING)
  private EntryStatus status;
}
