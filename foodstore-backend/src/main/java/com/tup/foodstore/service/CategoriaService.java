package com.tup.foodstore.service;

import com.tup.foodstore.dto.categoria.CategoriaCreate;
import com.tup.foodstore.dto.categoria.CategoriaDto;
import com.tup.foodstore.dto.categoria.CategoriaEdit;

import java.util.List;

public interface CategoriaService {

    CategoriaDto create(CategoriaCreate dto);

    CategoriaDto update(Long id, CategoriaEdit dto);

    CategoriaDto findById(Long id);

    List<CategoriaDto> findAll();

    void delete(Long id);
}