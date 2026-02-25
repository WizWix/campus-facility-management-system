package io.github.wizwix.cfms.service.iface;

import io.github.wizwix.cfms.dto.request.dorm.RequestDormApply;
import io.github.wizwix.cfms.dto.response.dorm.ResponseDormApplyResult;
import io.github.wizwix.cfms.dto.response.dorm.ResponseDormFloor;
import io.github.wizwix.cfms.dto.response.dorm.ResponseDormMyApplication;
import io.github.wizwix.cfms.model.User;
import io.github.wizwix.cfms.model.enums.Gender;

import java.util.List;

public interface IDormService {
  ResponseDormApplyResult apply(User currentUser, RequestDormApply request);

  void cancelApplication(User user, Long applicationId);

  List<ResponseDormFloor> getDormRooms(Gender gender);

  List<ResponseDormMyApplication> getMyApplications(User user);
}
