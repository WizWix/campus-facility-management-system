package io.github.wizwix.cfms.global.config.dev;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.wizwix.cfms.global.config.dev.base.BaseDevLoader;
import io.github.wizwix.cfms.model.DormRoom;
import io.github.wizwix.cfms.repo.DormRoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@Slf4j
public class DormDevLoader extends BaseDevLoader<DormRoom> {
  private final DormRoomRepository repo;

  public DormDevLoader(ResourceLoader loader, ObjectMapper mapper, DormRoomRepository repo) {
    super(loader, mapper, DormRoom.class, "data/dev/dorm-rooms.jsonc");
    this.repo = repo;
  }

  @Override
  public void load() {
    if (repo.count() > 0) return;
    processItems(room -> {
      if (!repo.existsByRoomNumber(room.getRoomNumber())) {
        repo.save(room);
      }
    });
    log.info("Loaded dev dorm rooms: {} rooms", repo.count());
  }

  @Override
  public void unload() {
    repo.deleteAll();
    log.info("Unloaded dev dorm rooms");
  }
}
