package com.tup.foodstore.mapper;

import com.tup.foodstore.dto.usuario.UsuarioDto;
import com.tup.foodstore.dto.usuario.UsuarioEdit;
import com.tup.foodstore.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioDto toDto(Usuario usuario) {
        return new UsuarioDto(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getCelular(),
                usuario.getRol()
        );
    }

    public void updateEntity(Usuario usuario, UsuarioEdit dto) {
        if (dto.nombre() != null) {
            usuario.setNombre(dto.nombre());
        }

        if (dto.apellido() != null) {
            usuario.setApellido(dto.apellido());
        }

        if (dto.email() != null) {
            usuario.setEmail(dto.email());
        }

        if (dto.celular() != null) {
            usuario.setCelular(dto.celular());
        }
    }
}