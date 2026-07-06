package com.tup.foodstore.repository;

import com.tup.foodstore.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository
        extends BaseRepository<Categoria> {

    List<Categoria> findAllByEliminadoFalse();

    Optional<Categoria> findByIdAndEliminadoFalse(Long id);
}