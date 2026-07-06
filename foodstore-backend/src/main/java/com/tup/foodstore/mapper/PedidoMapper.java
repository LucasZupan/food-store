package com.tup.foodstore.mapper;

import com.tup.foodstore.dto.pedido.DetallePedidoDto;
import com.tup.foodstore.dto.pedido.PedidoDto;
import com.tup.foodstore.dto.pedido.PedidoEdit;
import com.tup.foodstore.model.DetallePedido;
import com.tup.foodstore.model.Pedido;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PedidoMapper {

    private final ProductoMapper productoMapper;

    public PedidoMapper(ProductoMapper productoMapper) {
        this.productoMapper = productoMapper;
    }

    public PedidoDto toDto(Pedido pedido) {

        Set<DetallePedidoDto> detalles = pedido.getDetalles()
                .stream()
                .map(this::detalleToDto)
                .collect(Collectors.toSet());

        return new PedidoDto(
                pedido.getId(),
                pedido.getFecha(),
                pedido.getEstado(),
                pedido.getTotal(),
                pedido.getFormaPago(),
                pedido.getUsuario().getId(),
                detalles
        );
    }

    private DetallePedidoDto detalleToDto(DetallePedido detalle) {
        return new DetallePedidoDto(
                detalle.getId(),
                detalle.getCantidad(),
                detalle.getSubtotal(),
                productoMapper.toDto(detalle.getProducto())
        );
    }

    public void updateEntity(Pedido pedido, PedidoEdit dto) {
        if (dto.estado() != null) {
            pedido.setEstado(dto.estado());
        }

        if (dto.formaPago() != null) {
            pedido.setFormaPago(dto.formaPago());
        }
    }
}