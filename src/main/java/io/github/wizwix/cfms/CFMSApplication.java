package io.github.wizwix.cfms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CFMSApplication {
  public static void main(String[] args) {
    SpringApplication.run(CFMSApplication.class, args);
  }
}
