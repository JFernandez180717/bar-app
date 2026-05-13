---
name: springboot-4
description: Spring Boot 4 backend development guidelines for Bar App, including hexagonal architecture, REST API conventions, validation, exception handling, database access, and coding standards. Trigger, When creating or modifying Java files.
license: MIT
metadata:
  author: JFernandez180717
  version: "1.0"
---

# 🧠 Skill: Spring Boot 4 with Hexagonal Architecture (Enterprise Standard)

This skill defines the architecture rules, development standards, and coding conventions for building Spring Boot 4 applications using strict hexagonal architecture.
The objective is to ensure clean separation of concerns, maintainability, scalability, testability, and consistency across the project.

---

# 🏗️ **Project Structure (MANDATORY)**

```
com.co.bar.bar_app
│
├── application
│   ├── dto
│   ├── mapper
│   ├── ports
│   │   ├── in
│   │   └── out
│   └── service
│
├── domain
│   ├── exception
│   └── model
│
├── infrastructure
│   ├── config
│   ├── input.rest
│   │   ├── dto
│   │   └── controllers
│   ├── mapper
│   └── output.persistence
│       ├── converter
│       ├── entity
│       ├── id
│       ├── repository
│       └── adapters
│
├── security
└── BarAppApplication
```

---

# 🧩 **Hexagonal Architecture Rules**

## 🔹 Layers

### 1. **domain (core)**

* Contains:
  * Domain models (`User`, `Product`, etc.)
  * Exceptions
* ❌ MUST NOT depend on any other layer

---

### 2. **application**

* Contains:

  * Use cases (`service`)
  * DTOs (`record`)
  * Mappers (MapStruct)
  * Ports (interfaces)

#### 📌 Ports

* Input:

  ```
  UsuarioInPort
  ```
* Output:

  ```
  UsuarioOutPort
  ```

✔ Mandatory rule:

> Port name = `DomainClass + InPort / OutPort`  
> Mappers name = `DomainClass + ApplicationMapper`

---

### 3. **infrastructure**

* Concrete implementations:
  * Controllers (REST)
  * Persistence (JPA)
  * Security (Spring Security)

---

# 📦 **Naming Conventions (CRITICAL)**

| Type            | Example                     |
|-----------------|-----------------------------|
| Dominio         | `Usuario`                   |
| Entity          | `UsuarioEntity`             |
| Adapter         | `UsuarioPersistenceAdapter` |
| InPort          | `UsuarioInPort`             |
| OutPort         | `UsuarioOutPort`            |
| DTO request     | `UsuarioRequestDto`         |
| DTO application | `UsuarioApplicationDto`     | 

---

# 🧾 **DTOs (Mandatory)**

✔ Use `record`

```Java
public record UsuarioDto(
    Long id,
    String nombre,
    String email
) {}
```

✔ Communication:

* `controller → application`: DTO
* `application → infrastructure`: DTO or primitives
* ❌ Never expose Entities
* ❌ Never expose Domain Classes

---

# 🔄 **MapStruct (Mandatory)**

Location:

```
application.mapper
infrastructure.mapper
```

Example:

```Java
@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toDomain(UsuarioDto dto);

    UsuarioDto toDto(Usuario domain);
}
```

---

# 🔌 **Ports**

## InPort (use case)

```java
public interface UsuarioInPort {
    UsuarioDto create(UsuarioDto dto);
}
```

---

## OutPort (persistence)

```java
public interface UsuarioOutPort {
    Usuario create(Usuario usuario);
    Optional<Usuario> findById(Long id);
}
```

---

# ⚙️ **Service (Application Layer)**

```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioService implements UsuarioInPort {

    private final UsuarioOutPort usuarioOutPort;
    private final UsuarioMapper usuarioMapper;

    @Override
    @Transactional
    public UsuarioDto create(UsuarioDto dto) {
        Usuario usuario = usuarioMapper.toDomain(dto);
        Usuario guardado = usuarioOutPort.create(usuario);
        return usuarioMapper.toDto(guardado);
    }
}
```

✔ Rules:

* Stateless
* Use ports
* Handles transactions

---

# 🗄️ **Persistence (Infrastructure)**

## Entity

```java
@Entity
@Table(name = "usuario")
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    
    @Convert(converter = EstadoConverter.class)
    private boolean estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "usuario_crea", length = 30)
    private String usuarioCrea;

    @Column(name = "usuario_modifica", length = 30)
    private String usuarioModifica;
}
```

✔ RULES:
* ✔ Keep audit properties unless explicitly instructed that a specific class does not require them.

---

## Repository

```java
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
}
```

---

## Adapter (implements OutPort)

```java
@Component
@RequiredArgsConstructor
public class UsuarioPersistenceAdapter implements UsuarioOutPort {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity entity = mapper.toEntity(usuario);
        UsuarioEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }
}
```

✔ RULES:

* ❌ MUST NOT return Entities
* ✔ Returns domain models
* ✔ Accepts domain models or primitives

---

# 🌐 **Controllers (REST)**

Location:

```
infrastructure.input.rest
```

```java
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioInPort usuarioInPort;

    @PostMapping
    public ResponseEntity<UsuarioDto> create(@RequestBody @Valid UsuarioDto dto) {
        return ResponseEntity.ok(usuarioInPort.create(dto));
    }
}
```

---

# 📦 **ApiResponse (mandatory)**

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean error;
    private String message;
    private T data;
    private List<String> errors;
}
```

✔ All responses must be wrapped consistently. This is already handled by the `GlobalControllerAdvice` class, which is implemented using `@RestControllerAdvice`.

---

# 🔐 **Security (Spring Security)**

* Use `BCryptPasswordEncoder`
* Implement `UserDetails`. A class that implements `UserDetailsService` already exists: `UsuarioService`
* Typical class:

```java
@Service
public class UsuarioService implements UserDetailsService, IUserInPort {
}
```

✔ Never store passwords in plain text.

---

# ⚙️ **Configuration**

* Use:

  ```
  application.properties
  application-dev.properties
  application-prod.properties
  ```

✔ Activate profile:

```
spring.profiles.active=dev
```

---

# 🧪 **Testing**

* Unit tests: Mockito + JUnit 5
* Integration tests: `@SpringBootTest`
* Repository tests: `@DataJpaTest`

---

# 🪵 **Logging**

✔ Use parameterized logs:

```java
private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);
log.info("Usuario creado con id {}", id);
```

# 🚫 Forbidden Rules

❌ Do not use Entities outside the infrastructure layer  
❌ Do not bypass ports  
❌ Do not place business logic in controllers  
❌ Do not return Entities in the API  
❌ Do not couple layers  
❌ Do not return Domain Classes in the API  
❌ Do not return Domain Classes from the application layer to infrastructure layer  
❌ Method names, DTOs, variables, and other identifiers must always be in English (except for names that represent existing domain classes or entities)

---

# ✅ **Correct Flow**

```
Controller → InPort → Service → OutPort → Adapter → Repository → DB
```

---

# 🚀 Key Summary

* Strict hexagonal architecture
* DTOs must be implemented as `record`
* MapStruct is mandatory
* Ports must use clear and consistent naming
* Adapters must return domain models (never Entities)
* The API must always use `ApiResponse`

---
