package com.tup.foodstore.dto.categoria;

import jakarta.validation.constraints.Size;

public record CategoriaEdit(

        @Size(
                min = 2,
                max = 100,
                message = "El nombre debe tener entre 2 y 100 caracteres"
        )
        String nombre,

        @Size(
                max = 500,
                message = "La descripcion no puede superar los 500 caracteres"
        )
        String descripcion
) {
}