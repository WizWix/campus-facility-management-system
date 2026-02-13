package io.github.wizwix.cfms.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DevDataInitializer {
  @Bean
  @Profile("dev")
  public CommandLineRunner initDevData() {
    return args -> {
    };
  }

  @Bean
  @Profile("!dev")
  public CommandLineRunner removeDevData() {
    return args -> {
    };
  }
}
