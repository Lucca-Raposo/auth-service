package com.raposo.auth_service.config;

import com.raposo.auth_service.models.role.Role;
import com.raposo.auth_service.models.user.User;
import com.raposo.auth_service.repositories.RoleRepository;
import com.raposo.auth_service.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${admin-user.username}")
    private String username;

    @Value("${admin-user.password}")
    private String password;

    @Override
    @Transactional
    public void run(String... args) throws Exception{

        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());

        var userAdmin = userRepository.findByUsername(username);

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("User admin already exists!");
                },
                () -> {
                    var user = new User();
                    user.setUsername(username);
                    user.setPassword(passwordEncoder.encode(password));
                    user.setRole(roleAdmin);
                    userRepository.save(user);
                }
        );
    }
}
