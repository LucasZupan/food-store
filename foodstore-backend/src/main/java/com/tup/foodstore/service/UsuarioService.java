package com.tup.foodstore.service;

import com.tup.foodstore.dto.usuario.UsuarioDto;
import com.tup.foodstore.dto.usuario.UsuarioEdit;

import java.util.List;

public interface UsuarioService {

    List<UsuarioDto> findAll();

    UsuarioDto findById(Long id);

    UsuarioDto update(Long id, UsuarioEdit dto, String emailAutenticado, boolean esAdmin);

    void delete(Long id);
}