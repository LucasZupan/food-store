package com.tup.foodstore.config;

import com.tup.foodstore.model.Rol;
import com.tup.foodstore.model.Usuario;
import com.tup.foodstore.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserLoad implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UserLoad(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (!usuarioRepository.existsByEmail("admin@admin.com")) {

            Usuario admin = Usuario.builder()
                    .nombre("Admin")
                    .apellido("Sistema")
                    .email("admin@admin.com")
                    .celular("0000000000")
                    .password(passwordEncoder.encode("123456"))
                    .rol(Rol.ADMIN)
                    .build();

            usuarioRepository.save(admin);

            System.out.println("Usuario ADMIN creado: admin@admin.com / 123456");
        }
    }
}