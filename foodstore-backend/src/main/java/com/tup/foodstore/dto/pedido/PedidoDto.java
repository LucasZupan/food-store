package com.tup.foodstore.dto.pedido;

import com.tup.foodstore.model.Estado;
import com.tup.foodstore.model.FormaPago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record PedidoDto(

        Long id,
        LocalDate fecha,
        Estado estado,
        BigDecimal total,
        FormaPago formaPago,
        Long idUsuario,
        Set<DetallePedidoDto> detalles
) {
}