package com.tup.foodstore.mapper;

import com.tup.foodstore.dto.producto.ProductoCreate;
import com.tup.foodstore.dto.producto.ProductoDto;
import com.tup.foodstore.dto.producto.ProductoEdit;
import com.tup.foodstore.model.Categoria;
import com.tup.foodstore.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    private final CategoriaMapper categoriaMapper;

    public ProductoMapper(CategoriaMapper categoriaMapper) {
        this.categoriaMapper = categoriaMapper;
    }

    public Producto toEntity(ProductoCreate dto, Categoria categoria) {
        return Producto.builder()
                .nombre(dto.nombre())
                .precio(dto.precio())
                .descripcion(dto.descripcion())
                .stock(dto.stock())
                .imagen(dto.imagen())
                .disponible(dto.disponible() != null ? dto.disponible() : true)
                .categoria(categoria)
                .build();
    }

    public ProductoDto toDto(Producto producto) {
        return new ProductoDto(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getDescripcion(),
                producto.getStock(),
                producto.getImagen(),
                producto.getDisponible(),
                categoriaMapper.toDto(producto.getCategoria())
        );
    }

    public void updateEntity(Producto producto, ProductoEdit dto, Categoria categoria) {
        if (dto.nombre() != null) {
            producto.setNombre(dto.nombre());
        }

        if (dto.precio() != null) {
            producto.setPrecio(dto.precio());
        }

        if (dto.descripcion() != null) {
            producto.setDescripcion(dto.descripcion());
        }

        if (dto.stock() != null) {
            producto.setStock(dto.stock());
        }

        if (dto.imagen() != null) {
            producto.setImagen(dto.imagen());
        }

        if (dto.disponible() != null) {
            producto.setDisponible(dto.disponible());
        }

        if (categoria != null) {
            producto.setCategoria(categoria);
        }
    }
}