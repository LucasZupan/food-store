package com.tup.foodstore.repository;

import com.tup.foodstore.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository
        extends BaseRepository<Usuario> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findAllByEliminadoFalse();

    Optional<Usuario> findByIdAndEliminadoFalse(Long id);
}