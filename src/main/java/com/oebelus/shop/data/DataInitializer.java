package com.oebelus.shop.data;

import com.oebelus.shop.model.User;
import com.oebelus.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createDefaultUserIfNotExists();
    }

    private void createDefaultUserIfNotExists() {
        for (int i = 0; i < 5; i++) {
            String defaultEmail = "user"+i+"@email.com";

            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setEmail(defaultEmail);
            user.setFirstName("User");
            user.setLastName("User" + i);
            user.setPassword("password");
            userRepository.save(user);

            System.out.println("Created default user: " + defaultEmail);
        }
    }
}
