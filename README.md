# CinesHamm01

Proyecto de **Programación de 1º DAM** desarrollado en **Java** con **JavaFX**, **Maven** y **MySQL**.

La aplicación simula la gestión de un cine/autocine, permitiendo a los usuarios registrarse, iniciar sesión, consultar películas, realizar reservas y escribir reseñas. También incluye una zona de administración para gestionar usuarios, películas, coches, reservas y reseñas.

## Características principales

- Registro e inicio de sesión de usuarios.
- Contraseñas cifradas mediante SHA-256.
- Validación de datos como email, teléfono y matrícula.
- Diferenciación entre usuario normal y administrador.
- Gestión de películas.
- Gestión de coches.
- Gestión de reservas.
- Gestión de reseñas.
- Panel de administración.
- Conexión a base de datos MySQL mediante un archivo `conexion.xml`.
- Uso del patrón DAO para separar la lógica de acceso a datos.
- Interfaz gráfica creada con JavaFX y archivos FXML.
- Documentación generada con JavaDoc.
- Pruebas básicas con JUnit.

## Tecnologías utilizadas

- Java 11
- JavaFX 13
- Maven
- MySQL
- JDBC
- JAXB
- JUnit 5
- FXML
- CSS

## Estructura del proyecto

```text
CinesHamm01
├── src
│   ├── main
│   │   ├── java
│   │   │   └── org/Hamm
│   │   │       ├── Controller
│   │   │       ├── Logger
│   │   │       ├── Model
│   │   │       │   ├── Connections
│   │   │       │   ├── DAO
│   │   │       │   └── Domain
│   │   │       ├── Test
│   │   │       └── Utils
│   │   └── resources
│   │       └── org/Hamm/Controller
│   └── test
│       └── java
├── documentacion
├── conexion.xml
├── pom.xml
└── README.md
```

## Paquetes principales

### `Controller`

Contiene los controladores de las vistas JavaFX:

- `LoginController`
- `RegisterController`
- `MenuPrincipalController`
- `AdminController`
- `ListFilmController`
- `ListFilmControllerAdmin`
- `ListUserControllerAdmin`
- `ListCarControllerAdmin`
- `ListBookingsController`
- `ListBookingsControllerAdmin`
- `ReviewController`
- `ListReviewAdmin`
- `ChangeEmail`
- `EuroController`

### `Model/Domain`

Contiene las clases del modelo de datos:

- `User`
- `Admin`
- `Person`
- `Film`
- `Car`
- `Reservation`
- `Review`

### `Model/DAO`

Contiene las clases encargadas de acceder a la base de datos:

- `UserDAO`
- `FilmDAO`
- `CarDAO`
- `ReservationDAO`
- `ReviewDAO`
- `DAO`

### `Model/Connections`

Contiene las clases relacionadas con la conexión a MySQL:

- `ConnectionMySQL`
- `ConexionData`

### `Utils`

Contiene clases auxiliares:

- `PasswordUtils`
- `Validator`

### `Logger`

Contiene la clase `LoggerSingleton`, usada para registrar información en el archivo `logs.txt`.

## Base de datos

El proyecto utiliza una base de datos MySQL llamada:

```sql
CinesHamm
```

La configuración de conexión se encuentra en el archivo `conexion.xml`:

```xml
<conexion>
    <server>jdbc:mysql://localhost</server>
    <database>CinesHamm</database>
    <username>root</username>
    <password></password>
</conexion>
```

Antes de ejecutar la aplicación, es necesario tener MySQL iniciado y crear la base de datos correspondiente.

Ejemplo:

```sql
CREATE DATABASE CinesHamm;
USE CinesHamm;
```

Tablas usadas por el proyecto:

- `User`
- `Film`
- `Car`
- `Reservation`
- `review`

> Nota: el repositorio no incluye un archivo `.sql` completo de creación de tablas. Si se ejecuta el proyecto en otro equipo, será necesario crear la estructura de la base de datos según las clases DAO.

## Instalación

1. Clonar el repositorio:

```bash
git clone <url-del-repositorio>
```

2. Entrar en la carpeta del proyecto:

```bash
cd CinesHamm01
```

3. Comprobar que MySQL está iniciado.

4. Revisar el archivo `conexion.xml` y modificar usuario o contraseña si fuera necesario:

```xml
<username>root</username>
<password></password>
```

5. Instalar las dependencias con Maven:

```bash
mvn clean install
```

## Ejecución

El proyecto incluye una clase `App`, pero el propio código indica que para abrir la aplicación debe usarse la clase:

```text
org.Hamm.Test.Test2View
```

Desde un IDE como IntelliJ IDEA, se puede ejecutar directamente:

```text
src/main/java/org/Hamm/Test/Test2View.java
```

También puede ejecutarse desde Maven ajustando la clase principal si fuera necesario.

## Funcionalidades de usuario

Un usuario puede:

- Registrarse en la aplicación.
- Iniciar sesión.
- Ver el menú principal.
- Consultar la lista de películas.
- Realizar reservas.
- Consultar sus reservas.
- Escribir reseñas.
- Cambiar su email.

## Funcionalidades de administrador

Un administrador puede:

- Consultar usuarios.
- Modificar usuarios.
- Eliminar usuarios.
- Convertir usuarios en administradores.
- Gestionar películas.
- Gestionar coches.
- Gestionar reservas.
- Consultar y eliminar reseñas.

## Validaciones

La aplicación incluye validaciones para:

- Email.
- Teléfono.
- Matrícula.
- Campos vacíos.
- Contraseñas cifradas.

## Documentación

El proyecto incluye una carpeta `documentacion` con diagramas:

- Casos de uso.
- Diagrama de actividad.
- Modelo entidad-relación.

También incluye documentación JavaDoc generada en HTML.

## Pruebas

El proyecto contiene pruebas en:

```text
src/test/java/org/Hamm
```

Para ejecutar las pruebas:

```bash
mvn test
```

## Posibles mejoras

- Añadir un script SQL para crear la base de datos automáticamente.
- Corregir la clase principal del `pom.xml` para ejecutar directamente `Test2View`.
- Mejorar la separación entre clases de prueba y clases principales de la aplicación.
- Añadir más pruebas unitarias.
- Mejorar el diseño visual de algunas ventanas.
- Añadir control de errores más detallado en la interfaz.
- Añadir documentación sobre usuarios de prueba y administradores.

## Autor

Proyecto realizado por **Erich Hamm Hernández** para **Programación de 1º DAM**.
