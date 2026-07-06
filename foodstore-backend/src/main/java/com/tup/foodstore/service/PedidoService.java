package com.tup.foodstore.service;

import com.tup.foodstore.dto.pedido.PedidoCreate;
import com.tup.foodstore.dto.pedido.PedidoDto;
import com.tup.foodstore.dto.pedido.PedidoEdit;

import java.util.List;

public interface PedidoService {

    PedidoDto create(PedidoCreate dto, String emailUsuario);

    List<PedidoDto> findAll();

    PedidoDto findById(Long id, String emailUsuario, boolean esAdmin);

    List<PedidoDto> findByUsuarioId(Long usuarioId, String emailUsuario, boolean esAdmin);

    List<PedidoDto> findMisPedidos(String emailUsuario);

    PedidoDto update(Long id, PedidoEdit dto);

    void delete(Long id);
}