package io.github.wizwix.cfms.global.config;

import io.github.wizwix.cfms.global.error.JsonErrorResponseWriter;
import io.github.wizwix.cfms.global.ratelimit.PasswordResetRateLimitFilter;
import io.github.wizwix.cfms.global.security.JwtAuthenticationProcessingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final JsonErrorResponseWriter errorWriter;
  private final JwtAuthenticationProcessingFilter jwtAuthenticationFilter;
  private final PasswordResetRateLimitFilter rateLimitFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    AuthenticationEntryPoint entryPoint = (request, response, ex) ->
        errorWriter.write(response, HttpStatus.UNAUTHORIZED, "Authentication required");
    AccessDeniedHandler deniedHandler = (request, response, ex) ->
        errorWriter.write(response, HttpStatus.FORBIDDEN, "Access denied");

    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth

            // ── 인증 ──
            .requestMatchers(
                "/api/auth/login", "/api/auth/logout",
                "/api/auth/register", "/api/auth/password-reset/**"
            ).permitAll()

            // ── 관리자 전용 ──
            .requestMatchers("/api/admin/**").hasRole("ADMIN")

            // ── 로그인 필요 GET — permitAll() 보다 반드시 먼저 선언 ──
            // /api/clubs/me : 내 동아리 목록 (기존 /api/clubs/my 오타 수정)
            .requestMatchers(HttpMethod.GET,
                "/api/clubs/me",
                "/api/reservations/me",
                "/api/dorms/my",
                "/api/counseling/reservations/me",
                "/api/buildings/*/library/reading-rooms/reservations/me",
                "/api/buildings/*/library/study-rooms/reservations/me"
            ).authenticated()

            // ── 공개 GET ──
            // ※ 위의 .authenticated() 매처보다 뒤에 선언되어야 /me 경로가 덮이지 않음
            .requestMatchers(HttpMethod.GET,
                "/api/buildings/**", "/api/rooms/**", "/api/cafeterias/**",
                "/api/dorms/**", "/api/clubs/**", "/api/reservations",
                "/api/counseling/counselors", "/api/counseling/slots",
                "/api/library/**"
            ).permitAll()

            // ── 로그인 필요 (POST / PATCH / DELETE) ──
            .requestMatchers(HttpMethod.POST, "/api/clubs/**").authenticated()
            .requestMatchers(HttpMethod.PATCH, "/api/clubs/**").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/api/clubs/**").authenticated()

            // ── 나머지 인증 필요 API ──
            .requestMatchers("/api/users/**", "/api/reservations/**").authenticated()
            .requestMatchers("/api/**").authenticated()

            // ── 프론트엔드 (React SPA) 및 정적 리소스 ──
            .anyRequest().permitAll()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(rateLimitFilter, JwtAuthenticationProcessingFilter.class)
        .exceptionHandling(conf -> conf
            .authenticationEntryPoint(entryPoint)
            .accessDeniedHandler(deniedHandler)
        );
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}