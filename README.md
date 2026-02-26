# Tienda Verduras - Proyecto con Patrón DAO

Este proyecto corresponde a una aplicación de escritorio en **Java + Swing + JDBC** que implementa un sistema de gestión para una tienda de verduras.

El foco principal del proyecto es demostrar la aplicación del **Patrón DAO (Data Access Object)** como mecanismo de separación de responsabilidades entre:

- Lógica de negocio
- Acceso a datos
- Interfaz de usuario

---

#  Objetivo del Proyecto

Construir una aplicación que permita:

- Autenticación de usuarios
- Gestión de productos
- Gestión de categorías


Todo esto utilizando:

✔ Arquitectura por capas  
✔ JDBC  
✔ Base de datos relacional normalizada  
✔ Patrón DAO

---

#  Arquitectura del Proyecto

El sistema está estructurado en capas:

Vista (Swing)
↓
Controladores
↓
DAO (Acceso a datos)
↓
Base de Datos MySQL


---

#  Rol del Patrón DAO

El **DAO** es el corazón arquitectónico del proyecto.

Su función es:

 Encapsular toda la lógica SQL  
 Aislar la base de datos del resto del sistema  
 Permitir cambios en la persistencia sin afectar la lógica de negocio

Sin DAO, los controladores tendrían código SQL mezclado con reglas de negocio.

Con DAO:

| Capa | Responsabilidad |
|------|---------------|
| Vista | Interacción con usuario |
| Controlador | Lógica de negocio |
| DAO | Persistencia |
| BD | Almacenamiento |

---

#  DAO Implementados

El proyecto incluye:

| DAO | Responsabilidad |
|-----|----------------|
| `UsuarioDAO` | Autenticación y gestión de usuarios |
| `CategoriaDAO` | Gestión de categorías |
| `ProductoDAO` | CRUD de productos |
| `VentaDAO` | Registro de ventas |
| `DetalleVentaDAO` | Registro de detalle de ventas |

---

#  Configuración de Base de Datos

Antes de ejecutar la aplicación, debes crear la base de datos en MySQL.

Ejecuta el siguiente script completo en tu gestor MySQL:

##  Script de Base de Datos

```sql
-- Crear base de datos
CREATE DATABASE IF NOT EXISTS tienda_verduras;
USE tienda_verduras;

-- OJO: si existen, bórralas en orden por FK
DROP TABLE IF EXISTS detalle_venta;
DROP TABLE IF EXISTS ventas;
DROP TABLE IF EXISTS productos;
DROP TABLE IF EXISTS categorias;
DROP TABLE IF EXISTS usuarios;

-- Tabla de usuarios
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    contraseña VARCHAR(256) NOT NULL,
    rol ENUM('admin', 'empleado') NOT NULL DEFAULT 'empleado',
    activo BOOLEAN DEFAULT TRUE
);

-- Tabla de categorías
CREATE TABLE categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(50) NOT NULL
);

-- Tabla de productos
CREATE TABLE productos (
   id_producto INT AUTO_INCREMENT PRIMARY KEY,
   nombre VARCHAR(100) NOT NULL,
   id_categoria INT,
   stock INT DEFAULT 0,
   valor INT NOT NULL,
   FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
);

-- Tabla de ventas
CREATE TABLE ventas (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2)
);

-- Tabla detalle de venta
CREATE TABLE detalle_venta (
   id_detalle_venta INT AUTO_INCREMENT PRIMARY KEY,
   id_venta INT,
   id_producto INT,
   cantidad INT,
   precio_unitario DECIMAL(10,2),
   FOREIGN KEY (id_venta) REFERENCES ventas(id_venta),
   FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

USE tienda_verduras;

-- Categorías
INSERT INTO categorias (nombre_categoria) VALUES
  ('Frutas'),
  ('Verduras'),
  ('Tubérculos');

-- Usuarios
INSERT INTO usuarios (nombre, correo, contraseña, rol, activo) VALUES
   ('Admin General', 'admin@verduras.cl', SHA2('123', 256), 'admin', TRUE),
   ('Juan Pérez', 'juan@tienda.cl', SHA2('123', 256), 'empleado', TRUE);

-- Productos
INSERT INTO productos (nombre, id_categoria, stock, valor) VALUES
   ('Manzana', 1, 50, 300),
   ('Zanahoria', 2, 30, 200),
   ('Papa', 3, 100, 150);