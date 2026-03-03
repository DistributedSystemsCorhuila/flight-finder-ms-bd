# Flight Finder MS DB - API Documentation

## Descripción
Microservicio de base de datos para el sistema Flight Finder. Gestiona las operaciones CRUD de usuarios.

## Swagger UI
Una vez la aplicación esté corriendo, puedes acceder a la documentación interactiva de la API en:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Endpoints

### Users API

Base URL: `/api/users`

#### Registrar Usuario
Registra un nuevo usuario en el sistema.

- **URL**: `/api/users/register`
- **Método**: `POST`
- **Content-Type**: `application/json`

**Request Body:**
```json
{
  "firstName": "string",
  "lastName": "string",
  "username": "string",
  "email": "string",
  "password": "string"
}
```

**Response Success (200 OK):**
```json
{
  "id": 1,
  "firstName": "string",
  "lastName": "string",
  "username": "string"
}
```

**Response Error (409 Conflict):**
```json
"Username is already in use."
```
o
```json
"Email is already in use."
```

## Modelos

### UserRegistrationDTO
| Campo | Tipo | Descripción |
|-------|------|-------------|
| firstName | String | Nombre del usuario |
| lastName | String | Apellido del usuario |
| username | String | Nombre de usuario único |
| email | String | Correo electrónico único |
| password | String | Contraseña del usuario |

### UserDTO
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long | ID único del usuario |
| firstName | String | Nombre del usuario |
| lastName | String | Apellido del usuario |
| username | String | Nombre de usuario |

## Configuración

### Base de Datos
- **Tipo**: PostgreSQL
- **URL**: `jdbc:postgresql://localhost:5432/flight_finder_db`
- **Usuario**: `postgres`
- **Contraseña**: `postgres`

### Dependencias principales
- Spring Boot 3.2.4
- Spring Data JPA
- PostgreSQL Driver
- Springdoc OpenAPI (Swagger)
- Lombok
- MapStruct

## Cómo ejecutar

1. Asegúrate de que PostgreSQL esté corriendo en `localhost:5432`
2. Crea la base de datos:
   ```sql
   CREATE DATABASE flight_finder_db;
   ```
3. Ejecuta la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Accede a Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Estructura del Proyecto

```
src/main/java/com/flight_finder_ms_db/
├── FlightFinderMsDbApplication.java
├── controller/
│   └── UsersController.java
├── dto/
│   ├── UserDTO.java
│   └── UserRegistrationDTO.java
├── entity/
│   └── User.java
├── mapper/
│   └── UserMapper.java
├── repository/
│   └── UserRepository.java
└── service/
    ├── UserService.java
    └── Impl/
        └── UserServiceImpl.java
```

