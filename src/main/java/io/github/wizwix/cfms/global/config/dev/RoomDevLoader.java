package io.github.wizwix.cfms.global.config.dev;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.wizwix.cfms.global.config.dev.base.BaseDevLoader;
import io.github.wizwix.cfms.model.Room;
import io.github.wizwix.cfms.repo.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component("roomDevLoader")
@Profile("dev")
@Order(2)
@Slf4j
public class RoomDevLoader extends BaseDevLoader<Room> {
  private final RoomRepository repo;

  public RoomDevLoader(ResourceLoader loader, ObjectMapper mapper, RoomRepository repo) {
    super(loader, mapper, Room.class, "data/dev/rooms.jsonc");
    this.repo = repo;
  }

  @Override
  public void load() {
    processItems(room -> {
      if (room.getBuilding() != null && !repo.existsByBuildingAndName(room.getBuilding(), room.getName())) {
        repo.save(room);
        log.info("Loaded dev room: ({} / {} / {})", room.getBuilding().getSlug(), room.getName(), room.getType());
      }
    });
  }

  @Override
  public void unload() {
    processItems(room -> {
      if (room.getBuilding() == null) return;
      repo.getRoomsByBuildingSlug(room.getBuilding().getSlug()).stream()
          .filter(r -> r.getName().equals(room.getName()))
          .findFirst()
          .ifPresent(existing -> {
            if (existing.getType() == room.getType() && existing.getCapacity().equals(room.getCapacity())) {
              repo.delete(existing);
              log.info("Unloaded dev room: ({} / {} / {})", room.getBuilding().getSlug(), room.getName(), room.getType());
            }
          });
    });
  }
}
