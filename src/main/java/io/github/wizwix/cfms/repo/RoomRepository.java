package io.github.wizwix.cfms.repo;

import io.github.wizwix.cfms.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
  List<Room> getRoomsByBuildingSlug(String slug);
}
