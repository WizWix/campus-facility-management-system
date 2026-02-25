package io.github.wizwix.cfms.repo;

import io.github.wizwix.cfms.model.CafeteriaMeal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CafeteriaMealRepository extends JpaRepository<CafeteriaMeal, Long> {
  List<CafeteriaMeal> findByDate(LocalDate date);
}
