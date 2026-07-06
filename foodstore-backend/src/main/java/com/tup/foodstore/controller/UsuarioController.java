package com.tup.foodstore.controller;

import com.tup.foodstore.dto.usuario.UsuarioDto;
import com.tup.foodstore.dto.usuario.UsuarioEdit;
import com.tup.foodstore.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(
            UsuarioService usuarioService
    ) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioDto> findAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioDto findById(
            @PathVariable Long id
    ) {
        return usuarioService.findById(id);
    }

    @PutMapping("/{id}")
    public UsuarioDto update(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioEdit dto,
            Authentication authentication
    ) {
        boolean esAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        return usuarioService.update(
                id,
                dto,
                authentication.getName(),
                esAdmin
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(
            @PathVariable Long id
    ) {
        usuarioService.delete(id);
    }
}