package com.tup.foodstore.dto.pedido;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DetallePedidoCreate(

        @NotNull
        Long idProducto,

        @NotNull
        @Min(1)
        Integer cantidad
) {
}