# LiterAlura 📚

Una aplicación de consola desarrollada en Java con Spring Boot que permite gestionar una biblioteca digital. Los usuarios pueden buscar libros, registrar información de autores y realizar consultas avanzadas sobre la colección de libros almacenada.

## 🚀 Características

- **Búsqueda de libros**: Busca libros por título utilizando la API de Gutendx
- **Gestión de biblioteca**: Registra y lista libros y autores en una base de datos PostgreSQL
- **Consultas avanzadas**: 
  - Listar autores vivos en un año específico
  - Filtrar libros por idioma
  - Ver estadísticas de descargas
  - Top 10 libros más descargados
- **Interfaz de consola**: Menú interactivo fácil de usar

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Data JPA**
- **PostgreSQL**
- **Jackson (para procesamiento JSON)**
- **Maven** (gestión de dependencias)

## 📋 Requisitos Previos

- Java 17 o superior
- PostgreSQL 12 o superior
- Maven 3.6 o superior (o usar el wrapper incluido)

## ⚙️ Configuración

### 1. Base de Datos

1. Instalar PostgreSQL
2. Crear una base de datos llamada `libritos`:
```sql
CREATE DATABASE libritos;
```

### 2. Variables de Entorno

Configurar las siguientes variables de entorno:
```bash
export DB_USER=tu_usuario_postgresql
export DB_PASSWORD=tu_contraseña_postgresql
```

O en Windows:
```cmd
set DB_USER=tu_usuario_postgresql
set DB_PASSWORD=tu_contraseña_postgresql
```

### 3. Configuración de Aplicación

El archivo `application.properties` ya está configurado para:
- Conexión a PostgreSQL en localhost
- Creación automática de tablas
- Logging de consultas SQL

## 🚀 Instalación y Ejecución

### Opción 1: Usando Maven Wrapper (Recomendado)

```bash
# En Linux/macOS
./mvnw spring-boot:run

# En Windows
mvnw.cmd spring-boot:run
```

### Opción 2: Usando Maven instalado

```bash
mvn spring-boot:run
```

### Opción 3: Ejecutar JAR compilado

```bash
# Compilar
./mvnw clean package

# Ejecutar
java -jar target/literalura-0.0.1-SNAPSHOT.jar
```

## 📖 Uso de la Aplicación

Al ejecutar la aplicación, verás un menú interactivo con las siguientes opciones:

```
======================================
              LiterAlura
======================================

--- Selecciona una opción ---

1 - Buscar libro por título
2 - Listar libros registrados
3 - Listar autores registrados
4 - Listar autores vivos en un año dado
5 - Listar libros por idioma
6 - Listar los 10 libros más descargados
7 - Mostrar estadísticas de la base de datos

0 - Salir
```

### Funcionalidades Detalladas

#### 1. Buscar libro por título
- Busca libros en la API de Gutendx
- Guarda automáticamente el libro y autor en la base de datos
- Evita duplicados

#### 2. Listar libros registrados
- Muestra todos los libros almacenados localmente
- Incluye título, autor, idiomas y número de descargas

#### 3. Listar autores registrados
- Muestra todos los autores con sus libros asociados
- Incluye años de nacimiento y muerte

#### 4. Listar autores vivos en un año dado
- Filtra autores que estaban vivos en un año específico
- Útil para estudios históricos

#### 5. Listar libros por idioma
Idiomas disponibles:
- Inglés (en)
- Francés (fr)
- Alemán (de)
- Portugués (pt)
- Español (es)

#### 6. Top 10 libros más descargados
- Ranking basado en estadísticas de Gutendx

#### 7. Estadísticas de la base de datos
- Promedio, máximo y mínimo de descargas
- Total de libros registrados

## 🏗️ Estructura del Proyecto

```
src/main/java/com/domain/literalura/
├── LiteraluraApplication.java      # Clase principal
├── main/
│   └── Main.java                   # Lógica del menú y operaciones
├── model/                          # Entidades y DTOs
│   ├── Author.java
│   ├── AuthorData.java
│   ├── Book.java
│   ├── BookData.java
│   └── Data.java
├── repository/                     # Repositorios JPA
│   ├── AuthorRepository.java
│   └── BookRepository.java
└── service/                        # Servicios
    ├── ApiConsulter.java
    ├── DataConverter.java
    └── IDataConverter.java
```

## 🗄️ Modelo de Datos

### Entidad Book
- `id` (Long): Identificador único
- `title` (String): Título del libro
- `author` (Author): Autor asociado
- `languages` (List<String>): Idiomas disponibles
- `downloads` (int): Número de descargas

### Entidad Author
- `id` (Long): Identificador único
- `name` (String): Nombre del autor
- `birth_year` (String): Año de nacimiento
- `death_year` (String): Año de muerte
- `books` (List<Book>): Libros del autor

## 🔧 API Externa

La aplicación utiliza la [API de Gutendx](https://gutendx.com/) para buscar información de libros del Proyecto Gutenberg.

Endpoint utilizado:
```
https://gutendx.com/books/?search={título}
```

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📝 Licencia

Este proyecto está bajo la Licencia Apache 2.0. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Autor

Desarrollado como parte del Challenge LiterAlura de Oracle Next Education + Alura.

---

⭐ ¡No olvides dar una estrella al proyecto si te resultó útil!
