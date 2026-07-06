package com.tup.foodstore.dto.producto;

import com.tup.foodstore.dto.categoria.CategoriaDto;

import java.math.BigDecimal;

public record ProductoDto(

        Long id,
        String nombre,
        BigDecimal precio,
        String descripcion,
        Integer stock,
        String imagen,
        Boolean disponible,
        CategoriaDto categoria

) {
}