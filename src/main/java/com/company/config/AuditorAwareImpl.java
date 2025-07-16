//package com.company.config;
//
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.lang.NonNull;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//public class AuditorAwareImpl implements AuditorAware<String> {
//
//    @Override
//    @NonNull
//    public Optional<String> getCurrentAuditor() {
//        try {
//            var auth = SecurityContextHolder.getContext().getAuthentication();
//
//            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
//                return Optional.of(auth.getName());
//            }
//
//        } catch (Exception e) {
//            // ignored
//        }
//
//        return Optional.empty(); // Or: Optional.of("system") for default fallback
//    }
//}
