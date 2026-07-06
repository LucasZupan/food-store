package com.tup.foodstore.service.impl;

import com.tup.foodstore.dto.usuario.UsuarioDto;
import com.tup.foodstore.dto.usuario.UsuarioEdit;
import com.tup.foodstore.exception.BadRequestException;
import com.tup.foodstore.exception.ResourceNotFoundException;
import com.tup.foodstore.mapper.UsuarioMapper;
import com.tup.foodstore.model.Usuario;
import com.tup.foodstore.repository.UsuarioRepository;
import com.tup.foodstore.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.tup.foodstore.model.Rol;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(
            UsuarioRepository usuarioRepository,
            UsuarioMapper usuarioMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UsuarioDto> findAll() {
        return usuarioRepository.findAllByEliminadoFalse()
                .stream()
                .map(usuarioMapper::toDto)
                .toList();
    }

    @Override
    public UsuarioDto findById(Long id) {
        Usuario usuario = usuarioRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado")
                );

        return usuarioMapper.toDto(usuario);
    }

    @Override
    public UsuarioDto update(Long id, UsuarioEdit dto, String emailAutenticado, boolean esAdmin) {

        Usuario usuario = usuarioRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado")
                );

        if (!esAdmin && !usuario.getEmail().equals(emailAutenticado)) {
            throw new BadRequestException("No podés modificar datos de otro usuario");
        }

        if (dto.email() != null && !dto.email().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(dto.email())) {
                throw new BadRequestException("Ya existe un usuario con el email: " + dto.email());
            }
        }

        usuarioMapper.updateEntity(usuario, dto);

        if (dto.password() != null) {
            usuario.setPassword(passwordEncoder.encode(dto.password()));
        }

        usuario = usuarioRepository.save(usuario);

        return usuarioMapper.toDto(usuario);
    }

    @Override
    public void delete(Long id) {

        Usuario usuario = usuarioRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado")
                );

        usuario.setEliminado(true);

        usuarioRepository.save(usuario);
    }
}