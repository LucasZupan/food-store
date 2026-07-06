
# Food Store

Trabajo Práctico Integrador - Programación 3

Aplicación web de gestión y venta de productos gastronómicos desarrollada con arquitectura cliente-servidor.

---

# Descripción

Food Store permite a los usuarios registrarse, iniciar sesión, explorar productos, realizar compras y consultar el historial de pedidos.

Además, cuenta con un panel de administración desde donde es posible gestionar categorías, productos y pedidos.

La aplicación implementa autenticación mediante JWT y control de acceso basado en roles.

---

# Tecnologías Utilizadas

## Backend

* Java 21
* Spring Boot 3
* Spring Security
* JWT (JSON Web Token)
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* Lombok
* Swagger / OpenAPI

## Frontend

* TypeScript
* HTML5
* CSS3
* Vite

---

# Funcionalidades

## Usuario

* Registro de usuarios
* Inicio de sesión
* Visualización de productos
* Filtrado por categorías
* Búsqueda de productos
* Ordenamiento por nombre y precio
* Carrito de compras
* Confirmación de pedidos
* Historial de pedidos

## Administrador

* Dashboard administrativo
* Gestión de categorías

  * Alta
  * Modificación
  * Baja lógica
* Gestión de productos

  * Alta
  * Modificación
  * Baja lógica
  * Gestión de disponibilidad
* Gestión de pedidos

  * Consulta
  * Actualización de estado
  * Eliminación

---

# Roles

### ADMIN

Puede acceder al panel administrativo y gestionar toda la información del sistema.

### USUARIO

Puede navegar la tienda, realizar compras y consultar sus pedidos.

---

# Seguridad

La aplicación utiliza:

* Spring Security
* JWT para autenticación
* BCrypt para almacenamiento seguro de contraseñas
* Control de acceso basado en roles
* Sesiones stateless

---

# Arquitectura

La solución se encuentra dividida en dos proyectos independientes:

## Backend

Responsable de:

* Exponer la API REST
* Gestionar autenticación y autorización
* Persistir la información en PostgreSQL
* Aplicar reglas de negocio

## Frontend

Responsable de:

* Interfaz de usuario
* Consumo de la API REST
* Navegación
* Gestión de sesión del usuario

---

# Estructura del Proyecto

TP-Integrador/

├── foodstore-backend/

├── foodstore-frontend/

└── README.md

---

# Base de Datos

Motor utilizado:

* PostgreSQL

La aplicación incluye carga inicial de datos mediante DataLoader.

Al iniciar el backend se generan automáticamente:

* Usuario administrador
* Categorías iniciales
* Productos iniciales

---

# Ejecución del Backend

## Requisitos

* Java 21
* Maven
* PostgreSQL

## Configuración

## PostgreSQL

Crear una base de datos llamada:

foodstore

Ejemplo:

CREATE DATABASE foodstore;

Configurar la conexión a PostgreSQL en:

src/main/resources/application.properties

Ejemplo:

spring.datasource.url=jdbc:postgresql://localhost:5432/foodstore

spring.datasource.username=postgres

spring.datasource.password=postgres

## Ejecución

Desde la carpeta foodstore-backend:

mvn spring-boot:run

API disponible en:

http://localhost:8080

---

# Documentación API

Swagger:

http://localhost:8080/swagger-ui/index.html

---

# Ejecución del Frontend

## Requisitos

* Node.js
* npm

## Instalación

Desde la carpeta foodstore-frontend:

npm install

## Ejecución

npm run dev

Aplicación disponible en:

http://localhost:5173

---

# Flujo de Uso

## Usuario

1. Registrarse
2. Iniciar sesión
3. Explorar productos
4. Agregar productos al carrito
5. Confirmar pedido
6. Consultar historial de pedidos

## Administrador

1. Iniciar sesión
2. Acceder al panel administrativo
3. Gestionar categorías
4. Gestionar productos
5. Gestionar pedidos

---

# Características Implementadas

* Arquitectura cliente-servidor
* API REST
* Autenticación JWT
* Control de acceso por roles
* Persistencia con JPA/Hibernate
* PostgreSQL
* CRUD de categorías
* CRUD de productos
* Gestión de pedidos
* Baja lógica
* Auditoría de entidades
* Frontend desacoplado del backend
* Consumo de API mediante Fetch

---


# Autor

**Lucas Zupan**

Tecnicatura Universitaria en Programación

Universidad Tecnológica Nacional (UTN)
