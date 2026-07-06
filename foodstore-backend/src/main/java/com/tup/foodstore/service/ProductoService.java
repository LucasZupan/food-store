package com.tup.foodstore.service;

import com.tup.foodstore.dto.producto.ProductoCreate;
import com.tup.foodstore.dto.producto.ProductoDto;
import com.tup.foodstore.dto.producto.ProductoEdit;

import java.util.List;

public interface ProductoService {

    ProductoDto create(ProductoCreate dto);

    ProductoDto update(Long id, ProductoEdit dto);

    ProductoDto findById(Long id);

    List<ProductoDto> findAll();

    List<ProductoDto> findByCategoriaId(Long categoriaId);

    void delete(Long id);
}