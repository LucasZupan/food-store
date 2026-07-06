package com.tup.foodstore.mapper;

import com.tup.foodstore.dto.categoria.CategoriaCreate;
import com.tup.foodstore.dto.categoria.CategoriaDto;
import com.tup.foodstore.dto.categoria.CategoriaEdit;
import com.tup.foodstore.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public Categoria toEntity(CategoriaCreate dto) {
        return Categoria.builder()
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .build();
    }

    public CategoriaDto toDto(Categoria categoria) {
        return new CategoriaDto(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }

    public void updateEntity(Categoria categoria, CategoriaEdit dto) {
        if (dto.nombre() != null) {
            categoria.setNombre(dto.nombre());
        }

        if (dto.descripcion() != null) {
            categoria.setDescripcion(dto.descripcion());
        }
    }
}