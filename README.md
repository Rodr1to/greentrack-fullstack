# GreenTrack - Sistema de Gestión de Préstamos

![Backend CI](https://github.com/Rodr1to/greentrack-fullstack/actions/workflows/backend.yml/badge.svg)
![Frontend CI](https://github.com/Rodr1to/greentrack-fullstack/actions/workflows/frontend.yml/badge.svg)

Sistema Full Stack para gestión de préstamos de equipos tecnológicos.

## Tecnologías Utilizadas

**Backend:**
* Java 21 / Spring Boot 4.0.1
* Maven
* Spring Security + JWT (Autenticación y Autorización)
* Spring Data JPA (PostgreSQL)
* JUnit + Mockito (Testing)
* Swagger / OpenAPI

**Frontend:**
* Angular 21 
* Bootstrap (UI)
* RxJS (Observables & State Management)
* Ngx-Toastr (Notificaciones)

**Base de Datos:**
* PostgreSQL (Postgres.app & pgAdmin 4)

## Instalación y Ejecución del Proyecto

### Prerrequisitos
* Java JDK 17+
* Node.js (v18 o superior) y NPM
* PostgreSQL
* Maven (wrapper incluido)
* Git

## INSTALACIÓN Y CONFIGURACIÓN

### 1. Base de Datos (PostgreSQL)
El proyecto requiere una base de datos llamada `greentrack_db`.

1.  Abrir terminal Postgres.app
2.  Crear la base de datos:
    ```sql
    CREATE DATABASE greentrack_db;
    ```
3.  Ejecutar el script de inicialización ubicado en este repositorio:
    * Ruta: `/sql/database.sql`
    * Este script creará las tablas (`usuarios`, `equipos`, `prestamos`) e insertará los datos semilla.

### 2. Configuración del Backend
Configurar credenciales de conexión a la base de datos antes de iniciar el servidor.

1.  Archivo de configuración:
    `greentrack-backend/src/main/resources/application.properties`

2.  Editar las siguientes líneas con credenciales locales de PostgreSQL:

    ```properties
    # Configuración de Conexión a Base de Datos
    spring.datasource.url=jdbc:postgresql://localhost:5432/greentrack_db
    spring.datasource.username=TU_USUARIO_POSTGRES
    spring.datasource.password=TU_CONTRASEÑA_POSTGRES
    
    # Configuración JWT (No modificar para entorno de desarrollo)
    jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
    jwt.expiration=86400000
    ```

3.  Ejecutar el servidor:
    Desde la terminal, en la carpeta `greentrack-backend`:
    ```bash
    ./mvnw spring-boot:run
    ```
    (En Windows usar `mvnw.cmd spring-boot:run`)

    El servidor iniciará en el puerto **8080**.
    Documentación Swagger disponible en: `http://localhost:8080/swagger-ui.html`

### 3. Configuración Frontend (Angular)
1.  Abrir una terminal y navegar a la carpeta del frontend:
    ```bash
    cd greentrack-frontend
    ```
2.  Instalar dependencias del proyecto:
    ```bash
    npm install
    ```
3.  Inicie el servidor de desarrollo:
    ```bash
    ng serve
    ```
4.  Acceda a la aplicación web en: `http://localhost:4200`

## Credenciales de Prueba (Seed)

| Rol | Usuario | Contraseña | Permisos |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin` | `password` | Control total (Usuarios, Equipos, Préstamos) |
| **USER** | `rodrigo` | `password` | Visualizar inventario, Registrar préstamos propios |


## Ejecución de Pruebas

**Backend (Unit Tests):**
Ejecutar el siguiente comando en la carpeta `greentrack-backend`:
```bash
./mvnw test
```

## Funcionalidades Implementadas
* **Seguridad:** Login con JWT, Guards para protección de rutas y manejo de sesión.
* **Gestión de Equipos:** CRUD completo con validación de estados (Disponible/Prestado).
* **Préstamos:** Flujo de préstamo con validación de disponibilidad y devolución de equipos.
* **Usuarios:** Panel de administración para gestionar usuarios y roles.
* **Reportes:** Filtros dinámicos y resumen de stock en tiempo real.