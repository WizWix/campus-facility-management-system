package io.github.wizwix.cfms.config;

import io.github.wizwix.cfms.jwt.JwtAuthenticationFilter;
import io.github.wizwix.cfms.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
  private final AccessDeniedHandler jsonAccessDeniedHandler = ((request, response, accessDeniedException) -> {
    response.setContentType("application/json;charset=utf-8");
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.getWriter().write("{\"message\": \"You do not have permission to access this resource.\"}");
  });
  private final AuthenticationEntryPoint jsonAuthenticationEntryPoint = (request, response, authException) -> {
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write("{\"message\":\"Full authentication is required to access this resource.\"}");
  };
  private final JwtUtils jwtUtils;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // allow public API
            .requestMatchers("/api/auth/**", "/api/buildings/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/articles/**", "/api/categories/**").permitAll()
            // protect admin paths
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            // any other API call must be authenticated
            .requestMatchers("/api/**").authenticated()
            // everything else (React frontend, static files etc.) are permitted
            .anyRequest().permitAll()
        )
        .addFilterBefore(new JwtAuthenticationFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(conf -> conf.accessDeniedHandler(jsonAccessDeniedHandler).authenticationEntryPoint(jsonAuthenticationEntryPoint));
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
