package com.tup.foodstore.dto.pedido;

import com.tup.foodstore.model.Estado;
import com.tup.foodstore.model.FormaPago;

public record PedidoEdit(

        Estado estado,
        FormaPago formaPago
) {
}