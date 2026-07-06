package com.tup.foodstore.repository;

import com.tup.foodstore.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository
        extends BaseRepository<Producto> {

    List<Producto> findAllByEliminadoFalse();

    Optional<Producto> findByIdAndEliminadoFalse(Long id);

    List<Producto> findAllByCategoriaIdAndEliminadoFalse(Long categoriaId);

    List<Producto> findByDisponibleTrue();

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    boolean existsByCategoriaIdAndEliminadoFalse(Long categoriaId);
}