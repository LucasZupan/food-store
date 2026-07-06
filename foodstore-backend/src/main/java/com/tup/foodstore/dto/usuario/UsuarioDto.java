package com.tup.foodstore.dto.usuario;

import com.tup.foodstore.model.Rol;

public record UsuarioDto(
        Long id,
        String nombre,
        String apellido,
        String email,
        String celular,
        Rol rol
) {
}
