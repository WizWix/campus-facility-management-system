package io.github.wizwix.cfms.controller;

import io.github.wizwix.cfms.dto.request.auth.RequestLogin;
import io.github.wizwix.cfms.dto.request.auth.RequestRegister;
import io.github.wizwix.cfms.dto.response.auth.ResponseLogin;
import io.github.wizwix.cfms.jwt.JwtUtils;
import io.github.wizwix.cfms.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {
  private final JwtUtils jwtUtils;
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<ResponseLogin> login(@Valid @RequestBody RequestLogin req, HttpServletResponse response) {
    ResponseLogin result = userService.login(req);

    Cookie cookie = new Cookie("jwt_token", result.accessToken());
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge((int) jwtUtils.getExpiration() / 1000);

    response.addCookie(cookie);

    return ResponseEntity.ok(result);
  }

  @PostMapping("/logout")
  public void logout() {}

  @PatchMapping("/password-reset")
  public void passwordReset() {}

  @PostMapping("/password-reset/request")
  public void passwordResetRequest() {}

  @PostMapping("/password-reset/verify")
  public void passwordResetVerify() {}

  @PostMapping("/register")
  public void register(@Valid @RequestBody RequestRegister req, HttpServletResponse response) {}
}
