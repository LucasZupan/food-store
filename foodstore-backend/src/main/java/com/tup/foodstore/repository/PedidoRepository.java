package com.tup.foodstore.repository;

import com.tup.foodstore.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository
        extends BaseRepository<Pedido> {

    List<Pedido> findAllByEliminadoFalse();

    Optional<Pedido> findByIdAndEliminadoFalse(Long id);

    List<Pedido> findAllByUsuarioIdAndEliminadoFalse(Long usuarioId);
}