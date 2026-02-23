package io.github.wizwix.cfms.repo;

import io.github.wizwix.cfms.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {}
