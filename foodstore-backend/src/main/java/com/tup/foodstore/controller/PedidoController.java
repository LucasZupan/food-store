package com.tup.foodstore.controller;

import com.tup.foodstore.dto.pedido.PedidoCreate;
import com.tup.foodstore.dto.pedido.PedidoDto;
import com.tup.foodstore.dto.pedido.PedidoEdit;
import com.tup.foodstore.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin("*")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoDto create(
            @Valid @RequestBody PedidoCreate dto,
            Authentication authentication
    ) {
        return pedidoService.create(dto, authentication.getName());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PedidoDto> findAll() {
        return pedidoService.findAll();
    }

    @GetMapping("/{id}")
    public PedidoDto findById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        boolean esAdmin = isAdmin(authentication);

        return pedidoService.findById(
                id,
                authentication.getName(),
                esAdmin
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<PedidoDto> findByUsuarioId(
            @PathVariable Long usuarioId,
            Authentication authentication
    ) {
        boolean esAdmin = isAdmin(authentication);

        return pedidoService.findByUsuarioId(
                usuarioId,
                authentication.getName(),
                esAdmin
        );
    }

    @GetMapping("/mis-pedidos")
    public List<PedidoDto> findMisPedidos(
            Authentication authentication
    ) {
        return pedidoService.findMisPedidos(authentication.getName());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PedidoDto update(
            @PathVariable Long id,
            @Valid @RequestBody PedidoEdit dto
    ) {
        return pedidoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(
            @PathVariable Long id
    ) {
        pedidoService.delete(id);
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}