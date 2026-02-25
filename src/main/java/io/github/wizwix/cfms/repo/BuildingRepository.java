package io.github.wizwix.cfms.repo;

import io.github.wizwix.cfms.model.Building;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {
  boolean existsBySlug(String slug);

  Optional<Building> findBySlug(String slug);
}
