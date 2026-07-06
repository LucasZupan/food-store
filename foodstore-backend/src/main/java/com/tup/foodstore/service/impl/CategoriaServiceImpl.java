package com.tup.foodstore.service.impl;

import com.tup.foodstore.dto.categoria.CategoriaCreate;
import com.tup.foodstore.dto.categoria.CategoriaDto;
import com.tup.foodstore.dto.categoria.CategoriaEdit;
import com.tup.foodstore.exception.ResourceNotFoundException;
import com.tup.foodstore.mapper.CategoriaMapper;
import com.tup.foodstore.model.Categoria;
import com.tup.foodstore.repository.CategoriaRepository;
import com.tup.foodstore.service.CategoriaService;
import org.springframework.stereotype.Service;
import com.tup.foodstore.exception.BadRequestException;
import com.tup.foodstore.repository.ProductoRepository;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    private final ProductoRepository productoRepository;

    public CategoriaServiceImpl(
            CategoriaRepository categoriaRepository,
            CategoriaMapper categoriaMapper,
            ProductoRepository productoRepository
    ) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
        this.productoRepository = productoRepository;
    }

    @Override
    public CategoriaDto create(CategoriaCreate dto) {

        Categoria categoria = categoriaMapper.toEntity(dto);

        categoria = categoriaRepository.save(categoria);

        return categoriaMapper.toDto(categoria);
    }

    @Override
    public CategoriaDto update(Long id, CategoriaEdit dto) {

        Categoria categoria = categoriaRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria no encontrada")
                );

        categoriaMapper.updateEntity(categoria, dto);

        categoria = categoriaRepository.save(categoria);

        return categoriaMapper.toDto(categoria);
    }

    @Override
    public CategoriaDto findById(Long id) {

        Categoria categoria = categoriaRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria no encontrada")
                );

        return categoriaMapper.toDto(categoria);
    }

    @Override
    public List<CategoriaDto> findAll() {

        return categoriaRepository.findAllByEliminadoFalse()
                .stream()
                .map(categoriaMapper::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {

        Categoria categoria = categoriaRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria no encontrada")
                );

        boolean tieneProductosActivos =
                productoRepository.existsByCategoriaIdAndEliminadoFalse(id);

        if (tieneProductosActivos) {
            throw new BadRequestException(
                    "No se puede eliminar la categoria porque tiene productos asociados"
            );
        }

        categoria.setEliminado(true);

        categoriaRepository.save(categoria);
    }
}