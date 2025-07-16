package com.company.config;

import com.company.entity.user.JwtUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import java.util.Optional;

@Configuration
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.getPrincipal() instanceof JwtUserDetails user) {
                return Optional.of(String.valueOf(user.userId())); // or Optional.of(String.valueOf(user.userId()))
            }

            return Optional.empty();
        };
    }
}
