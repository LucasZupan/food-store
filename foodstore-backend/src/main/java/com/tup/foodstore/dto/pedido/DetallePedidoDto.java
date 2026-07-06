package com.tup.foodstore.dto.pedido;

import com.tup.foodstore.dto.producto.ProductoDto;

import java.math.BigDecimal;

public record DetallePedidoDto(

        Long id,
        Integer cantidad,
        BigDecimal subtotal,
        ProductoDto producto
) {
}