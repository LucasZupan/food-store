package com.tup.foodstore.dto.pedido;

import com.tup.foodstore.model.Estado;
import com.tup.foodstore.model.FormaPago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoCreate(

        @NotNull
        Estado estado,

        @NotNull
        FormaPago formaPago,

        @NotEmpty
        List<@Valid DetallePedidoCreate> detallePedido
) {
}