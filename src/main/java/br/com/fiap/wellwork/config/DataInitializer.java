package br.com.fiap.wellwork.config;

import br.com.fiap.wellwork.model.entity.User;
import br.com.fiap.wellwork.model.enums.Role;
import br.com.fiap.wellwork.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.findByUsername("admin").isEmpty()){
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_USER));
            userRepository.save(admin);
        }
        if(userRepository.findByUsername("user").isEmpty()){
            User u = new User();
            u.setUsername("user");
            u.setPassword(passwordEncoder.encode("user123"));
            u.setRoles(Set.of(Role.ROLE_USER));
            userRepository.save(u);
        }
    }
}
