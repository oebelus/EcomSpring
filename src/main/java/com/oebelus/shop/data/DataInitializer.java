package com.oebelus.shop.data;

import com.oebelus.shop.model.Role;
import com.oebelus.shop.model.User;
import com.oebelus.shop.repository.RoleRepository;
import com.oebelus.shop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Transactional
@RequiredArgsConstructor
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");

        createDefaultRolesIfNotExists(defaultRoles);

        createDefaultUsersIfNotExists();
        createDefaultAdminsIfNotExists();
    }

    private void createDefaultUsersIfNotExists() {
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("User role not found"));

        for (int i = 0; i < 5; i++) {
            String defaultEmail = "user" + i + "@email.com";

            if (userRepository.existsByEmail(defaultEmail)) {
                continue;  // Skip if the user already exists
            }

            User user = new User();
            user.setEmail(defaultEmail);
            user.setFirstName("User");
            user.setLastName("User" + i);
            user.setPassword(passwordEncoder.encode("password"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);

            System.out.println("Created default user: " + defaultEmail);
        }
    }

    private void createDefaultAdminsIfNotExists() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new RuntimeException("Admin role not found"));

        for (int i = 0; i < 5; i++) {
            String defaultEmail = "admin" + i + "@email.com";

            if (userRepository.existsByEmail(defaultEmail)) {
                continue;  // Skip if the admin already exists
            }

            User admin = new User();
            admin.setEmail(defaultEmail);
            admin.setFirstName("Admin");
            admin.setLastName("Admin" + i);
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);

            System.out.println("Created default admin: " + defaultEmail);
        }
    }

    private void createDefaultRolesIfNotExists(Set<String> roles) {
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role::new).forEach(roleRepository::save);
    }
}
