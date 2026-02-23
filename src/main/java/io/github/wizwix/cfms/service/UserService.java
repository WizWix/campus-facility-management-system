package io.github.wizwix.cfms.service;

import io.github.wizwix.cfms.dto.request.auth.RequestLogin;
import io.github.wizwix.cfms.dto.response.auth.ResponseLogin;
import io.github.wizwix.cfms.dto.response.auth.ResponseUserSimpleInfo;
import io.github.wizwix.cfms.jwt.JwtUtils;
import io.github.wizwix.cfms.model.User;
import io.github.wizwix.cfms.repo.UserRepository;
import io.github.wizwix.cfms.service.iface.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtils jwtUtils;

  @Override
  public ResponseLogin login(RequestLogin login) {
    User user = userRepository.findByNumberAndEnabledTrue(login.userNumber()).orElseThrow(() -> new IllegalArgumentException("User not found"));
    if (!passwordEncoder.matches(login.password(), user.getPassword())) {
      throw new IllegalArgumentException("Invalid password");
    }
    String token = jwtUtils.generateToken(user.getNumber(), List.of(user.getRole().name()));

    ResponseUserSimpleInfo userInfo = new ResponseUserSimpleInfo(
        user.getId(),
        user.getName(),
        user.getNumber(),
        user.getRole()
    );

    return new ResponseLogin(token, userInfo);
  }
}
