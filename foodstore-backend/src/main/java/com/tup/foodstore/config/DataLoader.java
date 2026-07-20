package com.tup.foodstore.config;

import com.tup.foodstore.model.Categoria;
import com.tup.foodstore.model.Producto;
import com.tup.foodstore.repository.CategoriaRepository;
import com.tup.foodstore.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public DataLoader(
            CategoriaRepository categoriaRepository,
            ProductoRepository productoRepository
    ) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) {

        if (categoriaRepository.findAllByEliminadoFalse().isEmpty()) {
            cargarCategorias();
        }

        if (productoRepository.findAllByEliminadoFalse().isEmpty()) {
            cargarProductos();
        }
    }

    private void cargarCategorias() {

        categoriaRepository.save(Categoria.builder()
                .nombre("Hamburguesas")
                .descripcion("Hamburguesas artesanales con carne premium")
                .build());

        categoriaRepository.save(Categoria.builder()
                .nombre("Pizzas")
                .descripcion("Pizzas al horno de barro")
                .build());

        categoriaRepository.save(Categoria.builder()
                .nombre("Empanadas")
                .descripcion("Empanadas caseras horneadas")
                .build());

        categoriaRepository.save(Categoria.builder()
                .nombre("Bebidas")
                .descripcion("Bebidas frías y calientes")
                .build());

        categoriaRepository.save(Categoria.builder()
                .nombre("Papas Fritas")
                .descripcion("Acompañamientos crujientes")
                .build());

        System.out.println("Categorías iniciales cargadas");
    }

    private void cargarProductos() {

        Map<String, Categoria> categorias = Map.of(
                "Hamburguesas", buscarCategoria("Hamburguesas"),
                "Pizzas", buscarCategoria("Pizzas"),
                "Empanadas", buscarCategoria("Empanadas"),
                "Bebidas", buscarCategoria("Bebidas"),
                "Papas Fritas", buscarCategoria("Papas Fritas")
        );

        productoRepository.save(Producto.builder()
                .nombre("Hamburguesa Clásica")
                .precio(new BigDecimal("4500"))
                .descripcion("Carne 150g, lechuga, tomate, cebolla morada y mayonesa")
                .stock(50)
                .imagen("https://foodish-api.com/images/burger/burger1.jpg")
                .disponible(true)
                .categoria(categorias.get("Hamburguesas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Hamburguesa Doble")
                .precio(new BigDecimal("6200"))
                .descripcion("Doble carne 150g c/u, cheddar, panceta y salsa barbacoa")
                .stock(40)
                .imagen("https://foodish-api.com/images/burger/burger2.jpg")
                .disponible(true)
                .categoria(categorias.get("Hamburguesas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Hamburguesa Completa")
                .precio(new BigDecimal("5500"))
                .descripcion("Carne 150g, huevo, jamón, queso, lechuga y tomate")
                .stock(30)
                .imagen("https://foodish-api.com/images/burger/burger3.jpg")
                .disponible(true)
                .categoria(categorias.get("Hamburguesas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Hamburguesa Veggie")
                .precio(new BigDecimal("5000"))
                .descripcion("Medallón de lentejas y quinoa, rúcula, tomato confitado y hummus")
                .stock(20)
                .imagen("https://imgs.search.brave.com/vUU8kRyBw8UOoDFUuZja2znED53OJK7yhKK0fOgySrg/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly93d3cu/aG9sYS5jb20vaG9y/aXpvbi9sYW5kc2Nh/cGUvNWYyY2RlMjUz/ODExLWhhbWJ1cmd1/ZXNhcy12ZWdnaWVz/LXQuanBnP2ltPVJl/c2l6ZT0oNjQwKSx0/eXBlPWRvd25zaXpl")
                .disponible(true)
                .categoria(categorias.get("Hamburguesas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Pizza Mozzarella")
                .precio(new BigDecimal("3800"))
                .descripcion("Mozzarella, salsa de tomate y orégano")
                .stock(25)
                .imagen("https://foodish-api.com/images/pizza/pizza1.jpg")
                .disponible(true)
                .categoria(categorias.get("Pizzas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Pizza Napolitana")
                .precio(new BigDecimal("4500"))
                .descripcion("Mozzarella, rodajas de tomate, ajo, aceitunas y albahaca")
                .stock(25)
                .imagen("https://foodish-api.com/images/pizza/pizza2.jpg")
                .disponible(true)
                .categoria(categorias.get("Pizzas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Pizza Fugazzeta")
                .precio(new BigDecimal("4800"))
                .descripcion("Mozzarella, cebolla caramelizada, jamón y aceitunas")
                .stock(20)
                .imagen("https://foodish-api.com/images/pizza/pizza3.jpg")
                .disponible(true)
                .categoria(categorias.get("Pizzas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Pizza Especial")
                .precio(new BigDecimal("5200"))
                .descripcion("Mozzarella, pepperoni, morrón, cebolla, huevo y aceitunas")
                .stock(15)
                .imagen("https://foodish-api.com/images/pizza/pizza4.jpg")
                .disponible(true)
                .categoria(categorias.get("Pizzas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Empanada de Carne")
                .precio(new BigDecimal("1200"))
                .descripcion("Carne picada, cebolla, huevo duro, aceitunas y especias")
                .stock(100)
                .imagen("https://imgs.search.brave.com/59eMoaWJDRojEIbZWP0bDyOXFyd-uZ67f1Od9J5OdJo/rs:fit:500:0:1:0/g:ce/aHR0cHM6Ly9pbWFn/LmJvbnZpdmV1ci5j/b20vZW1wYW5hZGFz/LWFyZ2VudGluYXMt/ZGUtY2FybmUtZm90/by1jZXJjYS5qcGc")
                .disponible(true)
                .categoria(categorias.get("Empanadas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Empanada de Pollo")
                .precio(new BigDecimal("1200"))
                .descripcion("Pollo desmenuzado, cebolla, morrón y crema")
                .stock(80)
                .imagen("https://imgs.search.brave.com/rzKU68f-NWlQMA1gEaSdY5ToiVPImNEDEeyt91Mu3V8/rs:fit:500:0:1:0/g:ce/aHR0cHM6Ly93d3cu/c3VwZXJwb2xsby5j/bC9pbWcvcmVjZXRh/cy9lbXBhbmFkYXMt/ZGUtcG9sbG8ud2Vi/cA")
                .disponible(true)
                .categoria(categorias.get("Empanadas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Empanada Jamón y Queso")
                .precio(new BigDecimal("1100"))
                .descripcion("Jamón cocido y mozzarella")
                .stock(90)
                .imagen("https://imgs.search.brave.com/7oHPZkFZ8_QQGTD7jyuHPv22MkZ0XOCuew9a7chmD3g/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9pbWFn/ZXMucmFwcGkuY29t/LmFyL3Byb2R1Y3Rz/L2Y1MWFiMWI4LTdh/MzUtNGZkZi1hYWRm/LWQyNmJmYjcyYTRk/NS5wbmc_ZD0zMDB4/MzAwJmU9d2VicCZx/PTEw")
                .disponible(false)
                .categoria(categorias.get("Empanadas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Empanada de Verdura")
                .precio(new BigDecimal("1100"))
                .descripcion("Espinaca, acelga, ricota y nuez moscada")
                .stock(60)
                .imagen("https://imgs.search.brave.com/CFN-cLWHvGEPaFwXsHGF1B06YLGa6dL6reSjESWoM1w/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9yZWNl/dGFzbmF0dXJhLmNv/bS5hci9zaXRlcy9y/ZWNldGFzbmF0dXJh/L2ZpbGVzL25hdHVy/YS1mb3Rvcy13ZWIt/ZW5lcm8tNjU1eDQ3/NWVtcGFuYWRhc19k/ZV92ZXJkdXJhLmpw/Zw")
                .disponible(true)
                .categoria(categorias.get("Empanadas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Coca-Cola 500ml")
                .precio(new BigDecimal("1500"))
                .descripcion("Gaseosa Coca-Cola original 500ml")
                .stock(200)
                .imagen("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT_DGdYmpX-4DxMXP0pfMbqrYXVcWMLIfedQI22GryUuUKF94QnWFIeajQ&s=10")
                .disponible(true)
                .categoria(categorias.get("Bebidas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Sprite 500ml")
                .precio(new BigDecimal("1500"))
                .descripcion("Gaseosa Sprite 500ml")
                .stock(180)
                .imagen("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ5o_wW6KUdYGRXH5C_W1j3IEirD-sFNbxIM4VBXsVw4er7VOhPlu8beB69&s=10")
                .disponible(true)
                .categoria(categorias.get("Bebidas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Agua Mineral 500ml")
                .precio(new BigDecimal("1000"))
                .descripcion("Agua mineral sin gas 500ml")
                .stock(300)
                .imagen("https://dispandistribuidora.agilecdn.com.br/8211_1.jpg?v=338-3114143016")
                .disponible(true)
                .categoria(categorias.get("Bebidas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Cerveza Artesanal 473ml")
                .precio(new BigDecimal("2500"))
                .descripcion("Cerveza artesanal rubia 473ml")
                .stock(100)
                .imagen("https://imgs.search.brave.com/SpDJCdalR7edohOLzhzqCCD0Qbaz5l-BC07in3yPWXY/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly90aHVt/YnMuZHJlYW1zdGlt/ZS5jb20vYi9pZGVu/dGlmaWNhY2klQzMl/QjNuLW9zY3VyYS1k/ZS1sYS1jZXJ2ZXph/LWRlbC1hcnRlLWVs/LXZpZHJpby00NDA1/MzcyMC5qcGc")
                .disponible(true)
                .categoria(categorias.get("Bebidas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Papas Fritas Clásicas")
                .precio(new BigDecimal("2500"))
                .descripcion("Papas fritas bastón crocantes con sal parrillera")
                .stock(70)
                .imagen("https://imgs.search.brave.com/in2PgvEBAwflH6lKfKgmkhjM5zsAZt_5CCF9WkXKB1Y/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly90My5m/dGNkbi5uZXQvanBn/LzAzLzc0LzE4LzIw/LzM2MF9GXzM3NDE4/MjA3N19oS3ZLS2pL/YW5JZkFacW4wSzhV/WUE3M2FIejZGTXo5/dC5qcGc")
                .disponible(true)
                .categoria(categorias.get("Papas Fritas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Papas con Cheddar y Panceta")
                .precio(new BigDecimal("3500"))
                .descripcion("Papas fritas bañadas en cheddar y panceta crocante")
                .stock(50)
                .imagen("https://imgs.search.brave.com/tK-SyeGMUaiGUWpfNv8IlBMG7CDpcdQyFoF9zzxS3Os/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9tb3Jp/eGUuY29tLmFyL2Zp/bGVzL3JlY2V0YXMv/dGh1bWJzL3BhcGFz/Y2hlZGRhcm1vcml4/ZTAyLmpwZw")
                .disponible(true)
                .categoria(categorias.get("Papas Fritas"))
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Aros de Cebolla")
                .precio(new BigDecimal("2800"))
                .descripcion("Aros de cebolla empanizados, servidos con salsa barbacoa")
                .stock(40)
                .imagen("https://imgs.search.brave.com/MbIsq2iRx49GR24H-kWPDldGSqyiU7O9Xzc1JGEw0_Q/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly93d3cu/Y2xhcmluLmNvbS9p/bWcvMTk2OS8xMi8z/MS9Ia1JQTEdKRWxf/MTI1Nng2MjBfXzEu/anBnIzE1ODQxMTM5/ODY3NDM")
                .disponible(true)
                .categoria(categorias.get("Papas Fritas"))
                .build());

        System.out.println("Productos iniciales cargados");
    }

    private Categoria buscarCategoria(String nombre) {
        return categoriaRepository.findAllByEliminadoFalse()
                .stream()
                .filter(categoria -> categoria.getNombre().equals(nombre))
                .findFirst()
                .orElseThrow();
    }
}