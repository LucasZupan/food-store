package com.tup.foodstore.service.impl;

import com.tup.foodstore.dto.producto.ProductoCreate;
import com.tup.foodstore.dto.producto.ProductoDto;
import com.tup.foodstore.dto.producto.ProductoEdit;
import com.tup.foodstore.exception.ResourceNotFoundException;
import com.tup.foodstore.mapper.ProductoMapper;
import com.tup.foodstore.model.Categoria;
import com.tup.foodstore.model.Producto;
import com.tup.foodstore.repository.CategoriaRepository;
import com.tup.foodstore.repository.ProductoRepository;
import com.tup.foodstore.service.ProductoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoMapper productoMapper;

    public ProductoServiceImpl(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository,
            ProductoMapper productoMapper
    ) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoMapper = productoMapper;
    }

    @Override
    public ProductoDto create(ProductoCreate dto) {

        Categoria categoria = categoriaRepository.findByIdAndEliminadoFalse(dto.idCategoria())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria no encontrada")
                );

        Producto producto = productoMapper.toEntity(dto, categoria);

        producto = productoRepository.save(producto);

        return productoMapper.toDto(producto);
    }

    @Override
    public ProductoDto update(Long id, ProductoEdit dto) {

        Producto producto = productoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado")
                );

        Categoria categoria = null;

        if (dto.idCategoria() != null) {
            categoria = categoriaRepository.findByIdAndEliminadoFalse(dto.idCategoria())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Categoria no encontrada")
                    );
        }

        productoMapper.updateEntity(producto, dto, categoria);

        producto = productoRepository.save(producto);

        return productoMapper.toDto(producto);
    }

    @Override
    public ProductoDto findById(Long id) {

        Producto producto = productoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado")
                );

        return productoMapper.toDto(producto);
    }

    @Override
    public List<ProductoDto> findAll() {

        return productoRepository.findAllByEliminadoFalse()
                .stream()
                .map(productoMapper::toDto)
                .toList();
    }

    @Override
    public List<ProductoDto> findByCategoriaId(Long categoriaId) {

        categoriaRepository.findByIdAndEliminadoFalse(categoriaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria no encontrada")
                );

        return productoRepository.findAllByCategoriaIdAndEliminadoFalse(categoriaId)
                .stream()
                .map(productoMapper::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {

        Producto producto = productoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado")
                );

        producto.setEliminado(true);

        productoRepository.save(producto);
    }
}