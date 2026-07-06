package com.tup.foodstore.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UsuarioEdit(

        @Size(min = 2, max = 50)
        String nombre,

        @Size(min = 2, max = 50)
        String apellido,

        @Email
        String email,

        @Size(max = 20)
        String celular,

        @Size(min = 6)
        String password
) {
}