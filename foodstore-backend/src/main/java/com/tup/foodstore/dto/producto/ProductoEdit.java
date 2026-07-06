package com.tup.foodstore.dto.producto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductoEdit(

        @Size(min = 2, max = 100)
        String nombre,

        @DecimalMin(value = "0.01")
        BigDecimal precio,

        @Size(max = 500)
        String descripcion,

        @Min(0)
        Integer stock,

        String imagen,

        Boolean disponible,

        Long idCategoria
) {
}