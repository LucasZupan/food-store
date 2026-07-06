package com.tup.foodstore.service.impl;

import com.tup.foodstore.dto.auth.AuthResponse;
import com.tup.foodstore.dto.auth.LoginRequest;
import com.tup.foodstore.dto.auth.RegisterRequest;
import com.tup.foodstore.exception.BadRequestException;
import com.tup.foodstore.exception.ResourceNotFoundException;
import com.tup.foodstore.model.Rol;
import com.tup.foodstore.model.Usuario;
import com.tup.foodstore.repository.UsuarioRepository;
import com.tup.foodstore.security.JwtService;
import com.tup.foodstore.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Ya existe un usuario con el email: " + request.email());
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.nombre())
                .apellido(request.apellido())
                .email(request.email())
                .celular(request.celular())
                .password(passwordEncoder.encode(request.password()))
                .rol(Rol.USUARIO)
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        String token = jwtService.generateToken(
                usuarioGuardado.getEmail(),
                usuarioGuardado.getRol().name()
        );

        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        String token = jwtService.generateToken(
                usuario.getEmail(),
                usuario.getRol().name()
        );

        return new AuthResponse(token);
    }
}