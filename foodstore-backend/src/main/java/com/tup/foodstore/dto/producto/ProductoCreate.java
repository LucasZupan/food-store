package com.tup.foodstore.dto.producto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductoCreate(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 100,
                message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01",
                message = "El precio debe ser mayor a 0")
        BigDecimal precio,

        @Size(max = 500)
        String descripcion,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0,
                message = "El stock no puede ser negativo")
        Integer stock,

        String imagen,

        Boolean disponible,

        @NotNull(message = "La categoria es obligatoria")
        Long idCategoria
) {
}