package com.tup.foodstore.controller;

import com.tup.foodstore.dto.categoria.CategoriaCreate;
import com.tup.foodstore.dto.categoria.CategoriaDto;
import com.tup.foodstore.dto.categoria.CategoriaEdit;
import com.tup.foodstore.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin("*")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(
            CategoriaService categoriaService
    ) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public CategoriaDto create(
            @Valid @RequestBody CategoriaCreate dto
    ) {
        return categoriaService.create(dto);
    }

    @GetMapping
    public List<CategoriaDto> findAll() {
        return categoriaService.findAll();
    }

    @GetMapping("/{id}")
    public CategoriaDto findById(
            @PathVariable Long id
    ) {
        return categoriaService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoriaDto update(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaEdit dto
    ) {
        return categoriaService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(
            @PathVariable Long id
    ) {
        categoriaService.delete(id);
    }
}