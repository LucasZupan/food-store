package com.tup.foodstore.service.impl;

import com.tup.foodstore.dto.pedido.DetallePedidoCreate;
import com.tup.foodstore.dto.pedido.PedidoCreate;
import com.tup.foodstore.dto.pedido.PedidoDto;
import com.tup.foodstore.dto.pedido.PedidoEdit;
import com.tup.foodstore.exception.BadRequestException;
import com.tup.foodstore.exception.ResourceNotFoundException;
import com.tup.foodstore.mapper.PedidoMapper;
import com.tup.foodstore.model.DetallePedido;
import com.tup.foodstore.model.Pedido;
import com.tup.foodstore.model.Producto;
import com.tup.foodstore.model.Usuario;
import com.tup.foodstore.repository.PedidoRepository;
import com.tup.foodstore.repository.ProductoRepository;
import com.tup.foodstore.repository.UsuarioRepository;
import com.tup.foodstore.service.PedidoService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final PedidoMapper pedidoMapper;

    public PedidoServiceImpl(
            PedidoRepository pedidoRepository,
            UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository,
            PedidoMapper pedidoMapper
    ) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.pedidoMapper = pedidoMapper;
    }

    @Override
    @Transactional
    public PedidoDto create(PedidoCreate dto, String emailUsuario) {

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado")
                );

        Pedido pedido = Pedido.builder()
                .fecha(LocalDate.now())
                .estado(dto.estado())
                .formaPago(dto.formaPago())
                .usuario(usuario)
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (DetallePedidoCreate detalleDto : dto.detallePedido()) {

            Producto producto = productoRepository.findByIdAndEliminadoFalse(detalleDto.idProducto())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Producto no encontrado")
                    );

            if (!Boolean.TRUE.equals(producto.getDisponible())) {
                throw new BadRequestException("El producto '" + producto.getNombre() + "' no está disponible para la venta");
            }

            if (!producto.tieneStockSuficiente(detalleDto.cantidad())) {
                throw new BadRequestException(
                        "Stock insuficiente para '" + producto.getNombre() +
                                "'. Disponible: " + producto.getStock() +
                                ", Solicitado: " + detalleDto.cantidad()
                );
            }

            BigDecimal subtotal = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(detalleDto.cantidad()));

            DetallePedido detalle = DetallePedido.builder()
                    .producto(producto)
                    .cantidad(detalleDto.cantidad())
                    .subtotal(subtotal)
                    .build();

            pedido.agregarDetalle(detalle);

            producto.reducirStock(detalleDto.cantidad());

            total = total.add(subtotal);
        }

        pedido.setTotal(total);

        pedido = pedidoRepository.save(pedido);

        return pedidoMapper.toDto(pedido);
    }

    @Override
    public List<PedidoDto> findAll() {

        return pedidoRepository.findAllByEliminadoFalse()
                .stream()
                .map(pedidoMapper::toDto)
                .toList();
    }

    @Override
    public PedidoDto findById(Long id, String emailUsuario, boolean esAdmin) {

        Pedido pedido = pedidoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pedido no encontrado")
                );

        if (!esAdmin && !pedido.getUsuario().getEmail().equals(emailUsuario)) {
            throw new BadRequestException("No podés consultar un pedido de otro usuario");
        }

        return pedidoMapper.toDto(pedido);
    }

    @Override
    public List<PedidoDto> findByUsuarioId(Long usuarioId, String emailUsuario, boolean esAdmin) {

        Usuario usuario = usuarioRepository.findByIdAndEliminadoFalse(usuarioId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado")
                );

        if (!esAdmin && !usuario.getEmail().equals(emailUsuario)) {
            throw new BadRequestException("No podés consultar pedidos de otro usuario");
        }

        return pedidoRepository.findAllByUsuarioIdAndEliminadoFalse(usuarioId)
                .stream()
                .map(pedidoMapper::toDto)
                .toList();
    }

    @Override
    public PedidoDto update(Long id, PedidoEdit dto) {

        Pedido pedido = pedidoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pedido no encontrado")
                );

        pedidoMapper.updateEntity(pedido, dto);

        pedido = pedidoRepository.save(pedido);

        return pedidoMapper.toDto(pedido);
    }

    @Override
    public void delete(Long id) {

        Pedido pedido = pedidoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pedido no encontrado")
                );

        pedido.setEliminado(true);

        pedidoRepository.save(pedido);
    }

    @Override
    public List<PedidoDto> findMisPedidos(String emailUsuario) {

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado")
                );

        return pedidoRepository.findAllByUsuarioIdAndEliminadoFalse(usuario.getId())
                .stream()
                .map(pedidoMapper::toDto)
                .toList();
    }
}