package io.github.wizwix.cfms.global.config.dev;

import io.github.wizwix.cfms.global.config.dev.base.BaseDevLoader;
import io.github.wizwix.cfms.model.Building;
import io.github.wizwix.cfms.repo.BuildingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@Slf4j
public class BuildingDevLoader extends BaseDevLoader<Building> {
  private final BuildingRepository repo;

  public BuildingDevLoader(ResourceLoader loader, BuildingRepository repo) {
    super(loader, Building.class, "data/dev/buildings.json");
    this.repo = repo;
  }

  @Override
  public void load() {
    processItems(building -> {
      if (!repo.existsByIdentifier(building.getIdentifier())) {
        repo.save(building);
        log.info("Loaded dev building: ({} / {} / {})", building.getName(), building.getIdentifier(), building.getInfo());
      }
    });
  }

  @Override
  public void unload() {
    processItems(building -> repo.findByIdentifier(building.getIdentifier()).ifPresent(existingBuilding -> {
      if (building.getName().equals(existingBuilding.getName()) && building.getInfo().equals(existingBuilding.getInfo())) {
        repo.delete(existingBuilding);
        log.info("Unloaded dev building: ({} / {} / {})", building.getName(), building.getIdentifier(), building.getInfo());
      }
    }));
  }
}
