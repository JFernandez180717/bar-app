# BarApp - Sistema de Gestión Integral para Bar

**Bar App** es una aplicación digital para la gestión y operación de un bar, que integra ventas, inventario, atención de mesas y análisis del negocio en un solo sistema. Su objetivo es optimizar la administración del bar, facilitar el trabajo de los meseros y mejorar la experiencia del cliente.

---

## 🚀 Tecnologías

| Capa | Tecnología |
|------|------------|
| **Backend** | Java 25, Spring Boot 4.0.2, Spring Security |
| **Base de Datos** | PostgreSQL 18+ |
| **Autenticación** | JWT |

---

## 🏗️ Arquitectura del Proyecto

### Backend - Arquitectura Hexagonal (Puertos y Adaptadores)

El backend sigue el patrón de **Arquitectura Hexagonal** para separar la lógica de negocio de las externalidades:

```
application/           # Casos de uso
├── dto/              # Data Transfer Objects
├── mapper/           # Mapeadores DTO <-> Dominio
├── ports/            # Interfaces (contratos)
│   ├── in/           # Puertos de entrada (acciones)
│   └── out/          # Puertos de salida (persistencia)
└── service/          # Implementación de casos de uso

domain/               # Núcleo del negocio
├── exception/        # Excepciones de negocio
└── model/            # Entidades del dominio

infrastructure/       # Adaptadores externos
├── config/           # Configuración global
├── input/            # Controladores REST
│   └── rest/
├── mapper/           # Mapeadores Entity <-> Domain
├── output/           # Implementación de puertos
│   └── persistence/  # JPA Repositories
└── security/         # Seguridad JWT, filtros
```

**Principios aplicados:**
- **Domain-Driven Design (DDD)**: El dominio es el núcleo
- **Inversión de dependencias**: Depende de abstracciones, no de concreciones
- **Puertos**: Interfaces que definen contratos
- **Adaptadores**: Implementaciones que conectan con sistemas

---

## 📋 Requisitos Previos

- **Java JDK** 25+
- **PostgreSQL** 18+
- **Maven** 3.9+ (opcional, incluye wrapper)

---

## 🛠️ Configuración del Proyecto

### 1. Clonar el proyecto

```bash
git clone https://github.com/JFernandez180717/bar-app.git
cd bar-app
```

### 2. Base de datos

1. Crear la base de datos en PostgreSQL:

```sql
CREATE DATABASE bar_app;
```

2. Ejecutar los scripts de configuración:

```bash
# Desde la raíz del proyecto
psql -U postgres -d bar_app -f scripts/create_all.sql
psql -U postgres -d bar_app -f scripts/insert_roles.sql
psql -U postgres -d bar_app -f scripts/insert_tipo_identificacion.sql
psql -U postgres -d bar_app -f scripts/insert_super_user.sql
psql -U postgres -d bar_app -f scripts/insert_usuario_rol.sql
```

### 3. Backend

```bash
cd back

# Ejecutar con Maven wrapper
./mvnw spring-boot:run

# O con Maven instalado
mvn spring-boot:run
```

El backend estará disponible en: `http://localhost:8080`

**Endpoints principales:**
- API REST: `http://localhost:8080/api`
- Documentación Swagger: `http://localhost:8080/api` (configurada)


---

## 📁 Estructura del Proyecto

```
sistema-bar/
├── back/                    # Backend (Spring Boot)
│   ├── src/main/java/
│   │   └── co/com/bar/bar_app/
│   │       ├── application/       # Casos de uso, puertos (hexagonal)
│   │       ├── domain/            # Modelos y excepciones
│   │       └── infrastructure/    # Adaptadores, controladores, seguridad
│   └── pom.xml
│
├── scripts/                 # Scripts SQL
│   └── create all.sql       # Schema completo
│
└── README.md
```

---

## 🔐 Autenticación

- El backend retorna un token JWT en el header `Authorization`
- El frontend debe enviar el token en el header `Authorization: Bearer <token>`

---

## 🧪 Scripts SQL Disponibles

| Script | Descripción |
|--------|-------------|
| `create all.sql` | Schema completo de la base de datos |
| `insert_roles.sql` | Roles iniciales (ADMIN, MESERO, CAJERO) |
| `insert_tipo_identificacion.sql` | Tipos de identificación |
| `insert_super_user.sql` | Usuario administrador por defecto |
| `insert_usuario_rol.sql` | Relación usuario-rol |

---

## 📝 Licencia

MIT
