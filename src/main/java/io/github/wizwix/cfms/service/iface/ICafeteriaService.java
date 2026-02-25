package io.github.wizwix.cfms.service.iface;

import io.github.wizwix.cfms.dto.response.cafeteria.ResponseCafeteria;
import io.github.wizwix.cfms.dto.response.cafeteria.ResponseFoodCourtStore;

import java.time.LocalDate;
import java.util.List;

public interface ICafeteriaService {
  ResponseCafeteria getMeals(LocalDate date);
  List<ResponseFoodCourtStore> getFoodCourtStores();
}
