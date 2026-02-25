package io.github.wizwix.cfms.service.iface;

import io.github.wizwix.cfms.dto.request.auth.RequestLogin;
import io.github.wizwix.cfms.dto.request.auth.RequestPasswordResetConfirm;
import io.github.wizwix.cfms.dto.request.auth.RequestPasswordResetRequest;
import io.github.wizwix.cfms.dto.request.auth.RequestPasswordResetVerify;
import io.github.wizwix.cfms.dto.request.auth.RequestRegister;
import io.github.wizwix.cfms.dto.request.auth.RequestUserUpdate;
import io.github.wizwix.cfms.dto.response.auth.ResponseLogin;
import io.github.wizwix.cfms.dto.response.auth.ResponseUserProfile;

public interface IUserService {
  void confirmPasswordReset(RequestPasswordResetConfirm request);

  ResponseUserProfile getProfile(String userNumber);

  ResponseLogin login(RequestLogin login);

  void register(RequestRegister request);

  void requestPasswordReset(RequestPasswordResetRequest request);

  void updateProfile(String userNumber, RequestUserUpdate request);

  void verifyPasswordReset(RequestPasswordResetVerify request);
}
