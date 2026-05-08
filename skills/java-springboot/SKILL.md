---
name: springboot-4
description: >
  Description of the project architecture and rules.
  Trigger: When creating a new java file or modifying any java file.
license: 
metadata:
  author: JFernandez180717
  version: "1.0"
---

# 🧠 **Skill: Spring Boot 4 con Arquitectura Hexagonal (Estándar Empresarial)**

Tu objetivo es ayudar a desarrollar aplicaciones **Spring Boot 4** siguiendo arquitectura hexagonal estricta, asegurando separación de responsabilidades, mantenibilidad y escalabilidad.

---

# 🏗️ **Estructura del Proyecto (OBLIGATORIA)**

Basado en la imagen:

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

# 🧩 **Reglas de Arquitectura Hexagonal**

## 🔹 Capas

### 1. **domain (núcleo)**

* Contiene:

  * Modelos de dominio (`Usuario`, `Producto`, etc.)
  * Excepciones
* ❌ NO depende de ninguna otra capa

---

### 2. **application**

* Contiene:

  * Casos de uso (`service`)
  * DTOs (`record`)
  * Mappers (MapStruct)
  * Puertos (interfaces)

#### 📌 Puertos

* Entrada:

  ```
  UsuarioInPort
  ```
* Salida:

  ```
  UsuarioOutPort
  ```

✔ Regla obligatoria:

> Nombre puertos = `ClaseDominio + InPort / OutPort`
> Nombre mappers= `ClaseDominio + ApplicationMapper`

---

### 3. **infrastructure**

* Implementaciones concretas:

  * Controllers (REST)
  * Persistencia (JPA)
  * Seguridad (Spring Security)

---

# 📦 **Convenciones de Nombres (CRÍTICO)**

| Tipo            | Ejemplo                     |
| --------------- | --------------------------- |
| Dominio         | `Usuario`                   |
| Entity          | `UsuarioEntity`             |
| Adapter         | `UsuarioPersistenceAdapter` |
| InPort          | `UsuarioInPort`             |
| OutPort         | `UsuarioOutPort`            |
| DTO request     | `UsuarioRequestDto`         |
| DTO application | `UsuarioApplicationDto`     | 

---

# 🧾 **DTOs (OBLIGATORIO)**

✔ Usar `record`

```java
public record UsuarioDto(
    Long id,
    String nombre,
    String email
) {}
```

✔ Comunicación:

* `controller → application`: DTO
* `application → infrastructure`: DTO o primitivos
* ❌ Nunca exponer Entities
* ❌ Nunca exponer Clases de dominio

---

# 🔄 **MapStruct (OBLIGATORIO)**

Ubicación:

```
application.mapper
infrastructure.mapper
```

Ejemplo:

```java
@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toDomain(UsuarioDto dto);

    UsuarioDto toDto(Usuario domain);
}
```

---

# 🔌 **Puertos**

## InPort (caso de uso)

```java
public interface UsuarioInPort {
    UsuarioDto create(UsuarioDto dto);
}
```

---

## OutPort (persistencia)

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

✔ Reglas:

* Stateless
* Usa puertos
* Maneja transacciones

---

# 🗄️ **Persistencia (Infrastructure)**

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

✔ REGLAS:
* ✔ Mantener las propiedades de auditoria a menos que se te pida que una clase especifica no los necesita

---

## Repository

```java
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
}
```

---

## Adapter (IMPLEMENTA OutPort)

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

✔ REGLAS:

* ❌ NO retorna Entities
* ✔ retorna dominio
* ✔ recibe dominio o primitivos

---

# 🌐 **Controllers (REST)**

Ubicación:

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

# 📦 **ApiResponse (OBLIGATORIO)**

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

✔ Todas las respuestas deben ir encapsuladas, esto ya lo hace la clase GlobalControllerAdvice que es un @RestControllerAdvice

---

# 🔐 **Seguridad (Spring Security)**

* Usar `BCryptPasswordEncoder`
* Implementar `UserDetails`, ya existe una clase que implementa el UserDetailService es la clase UsuarioService
* Clase típica:

```java
@Service
public class UsuarioService implements UserDetailsService, IUserInPort {
}
```

✔ Nunca guardar contraseñas en texto plano

---

# ⚙️ **Configuración**

* Usar:

  ```
  application.properties
  application-dev.properties
  application-prod.properties
  ```

✔ Activar profile:

```
spring.profiles.active=dev
```

---

# 🧪 **Testing**

* Unitarios: Mockito + JUnit 5
* Integración: `@SpringBootTest`
* Repositorios: `@DataJpaTest`

---

# 🪵 **Logging**

```java
private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);
```

✔ Usar logs parametrizados:

```java
log.info("Usuario creado con id {}", id);
```

---

# 🚫 **Reglas PROHIBIDAS**

❌ No Usar Entities fuera de infraestructura
❌ No Saltarse puertos
❌ No Lógica en controllers
❌ No Retornar Entities en API
❌ No Acoplar capas
❌ No Retornar Clases de dominio en API
❌ No Retornar Clases de dominio desde appication a rest
❌ Nombres de metodos, DTOs, variables (excepto las que identifiquen una clase de dominio o entity) siempre en ingles

---

# ✅ **Flujo Correcto**

```
Controller → InPort → Service → OutPort → Adapter → Repository → DB
```

---

# 🚀 **Resumen Clave**

* Arquitectura hexagonal estricta
* DTOs como `record`ss
* MapStruct obligatorio
* Puertos bien nombrados
* Adapter retorna dominio (nunca entity)
* API siempre usa `ApiResponse`

---
