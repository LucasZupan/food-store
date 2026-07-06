package com.tup.foodstore.controller;

import com.tup.foodstore.dto.producto.ProductoCreate;
import com.tup.foodstore.dto.producto.ProductoDto;
import com.tup.foodstore.dto.producto.ProductoEdit;
import com.tup.foodstore.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin("*")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(
            ProductoService productoService
    ) {
        this.productoService = productoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ProductoDto create(
            @Valid @RequestBody ProductoCreate dto
    ) {
        return productoService.create(dto);
    }

    @GetMapping
    public List<ProductoDto> findAll() {
        return productoService.findAll();
    }

    @GetMapping("/{id}")
    public ProductoDto findById(
            @PathVariable Long id
    ) {
        return productoService.findById(id);
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<ProductoDto> findByCategoriaId(
            @PathVariable Long categoriaId
    ) {
        return productoService.findByCategoriaId(categoriaId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductoDto update(
            @PathVariable Long id,
            @Valid @RequestBody ProductoEdit dto
    ) {
        return productoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(
            @PathVariable Long id
    ) {
        productoService.delete(id);
    }
}