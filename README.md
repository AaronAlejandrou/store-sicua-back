# SICUA Backend - Sistema de Inventario y Control de Ventas

Backend desarrollado con **Spring Boot 3.2+**, **MySQL 8.0+** y arquitectura **Domain Driven Design (DDD)** para el sistema SICUA.

## 🚀 Características

- ✅ Arquitectura DDD (Domain Driven Design)
- ✅ Spring Boot 3.2+ con Java 17
- ✅ MySQL 8.0+ como base de datos
- ✅ APIs REST bien documentadas
- ✅ Validaciones con Bean Validation
- ✅ Manejo de transacciones
- ✅ CORS configurado para frontend
- ✅ Logging con SLF4J
- ✅ Tests unitarios
- ✅ Manejo global de excepciones
- ✅ Documentación automática con Swagger/OpenAPI 3

## 📋 Requisitos Previos

- **Java 17** o superior
- **Maven 3.8+**
- **MySQL 8.0+**
- **Git**

## 🛠️ Instalación y Configuración

### 1. Clonar el repositorio
```bash
git clone <repository-url>
cd store-sicua-back
```

### 2. Configurar la base de datos
```sql
-- Crear la base de datos
CREATE DATABASE sicua_db;

-- Crear usuario (opcional)
CREATE USER 'sicua_user'@'localhost' IDENTIFIED BY 'sicua_password';
GRANT ALL PRIVILEGES ON sicua_db.* TO 'sicua_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configurar application.properties
Actualizar las credenciales de la base de datos en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sicua_db
spring.datasource.username=root
spring.datasource.password=tu_password
```

### 4. Ejecutar el script de inicialización
Aplicar el script SQL ubicado en `src/main/resources/schema.sql` para crear las tablas necesarias.

### 5. Compilar y ejecutar
```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar tests
mvn test

# Ejecutar la aplicación
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 📖 Documentación de API (Swagger)

Una vez que la aplicación esté ejecutándose, puedes acceder a la documentación interactiva de la API:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

La documentación incluye:
- 📋 Descripción detallada de todos los endpoints
- 🔧 Interfaz interactiva para probar las APIs
- 📝 Ejemplos de requests y responses
- ✅ Códigos de estado y posibles errores
- 🏷️ Esquemas de datos con validaciones

## 📚 API Endpoints

### Products
- `GET /api/products` - Obtener todos los productos
- `POST /api/products` - Crear producto
- `PUT /api/products/{id}` - Actualizar producto
- `DELETE /api/products/{id}` - Eliminar producto

### Sales
- `GET /api/sales` - Obtener todas las ventas
- `POST /api/sales` - Crear venta
- `PUT /api/sales/{id}/invoice` - Marcar como facturada

### Store Config
- `GET /api/store-config` - Obtener configuración
- `PUT /api/store-config` - Actualizar configuración

## 🏗️ Arquitectura DDD

```
src/main/java/com/sicua/
├── SicuaApplication.java
├── domain/                    # Capa de Dominio
│   ├── product/
│   ├── sale/
│   └── storeconfig/
├── infrastructure/            # Capa de Infraestructura
│   ├── persistence/
│   └── config/
├── application/              # Capa de Aplicación
│   ├── product/
│   ├── sale/
│   └── storeconfig/
└── interfaces/               # Capa de Interfaces
    ├── rest/
    └── dto/
```

### Responsabilidades por Capa

**Domain**: Contiene las entidades de negocio, value objects, repositorios (interfaces) y servicios de dominio.

**Application**: Casos de uso de la aplicación, DTOs de entrada y salida.

**Infrastructure**: Implementaciones técnicas (persistencia, configuraciones).

**Interfaces**: Controladores REST y DTOs de respuesta.

## 🧪 Testing

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar tests con cobertura
mvn test jacoco:report
```

## 🔒 Seguridad y CORS

El backend está configurado para aceptar requests desde `http://localhost:5174` (frontend React).

Para cambiar la URL permitida, modificar `sicua.cors.allowed-origins` en `application.properties`.

## 📝 Logs

Los logs se muestran en consola con el formato estándar de Spring Boot. Los errores y operaciones importantes son registrados con SLF4J.

## 🚀 Despliegue

### Desarrollo
```bash
mvn spring-boot:run
```

### Producción
```bash
# Generar JAR
mvn clean package

# Ejecutar JAR
java -jar target/store-sicua-back-0.0.1-SNAPSHOT.jar
```

## 🤝 Contribución

1. Fork el proyecto
2. Crear una rama de feature (`git checkout -b feature/AmazingFeature`)
3. Commit los cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## 📄 Licencia

Este proyecto está bajo una licencia privada - ver el archivo [LICENSE.md](LICENSE.md) para detalles.

## 📞 Soporte

Para soporte técnico o preguntas, contactar al equipo de desarrollo.

---

**SICUA Backend** - Sistema de Inventario y Control de Ventas
