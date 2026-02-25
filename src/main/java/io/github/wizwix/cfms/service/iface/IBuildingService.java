package io.github.wizwix.cfms.service.iface;

import io.github.wizwix.cfms.dto.response.building.ResponseBuilding;
import io.github.wizwix.cfms.dto.response.room.ResponseRoom;

import java.util.List;

public interface IBuildingService {
  List<ResponseBuilding> getBuildingList();

  List<ResponseRoom> getRoomsBySlug(String slug);
}
