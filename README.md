# 🏫 Aplicación de Gestión de Reservas de Aulas

Backend desarrollado con **Spring Boot**, **MySQL** y **Lombok**, junto a un frontend estático minimalista desarrollado con **HTML, CSS y JavaScript** vanilla.

---

## 🛠️ Tecnologías Utilizadas

- **Backend:** Java, Spring Boot (Data JPA, Web), Lombok, Driver MySQL.
- **Base de Datos:** MySQL / MariaDB (gestión mediante phpMyAdmin).
- **Frontend:** HTML5, CSS3 (Variables, Grid, Flexbox) y JavaScript ES6 (Fetch API).
- **Control de Versiones:** Git y GitHub Pages.

---

## 📐 Arquitectura de Entidades

1. **Aula:** Identificador, Nombre, Capacidad y presencia de ordenadores.
2. **Profesor:** Identificador, Nombre, Apellidos y Email único.
3. **FranjaHoraria:** Identificador, Hora Inicio, Hora Fin y Descripción.
4. **Reserva:** Identificador, Fecha de Reserva, Fecha de Creación (automática con `@PrePersist`), y relaciones `@ManyToOne` con `Aula`, `Profesor` y `FranjaHoraria`.

---

## 💡 Decisiones de Diseño y Propuestas de la IA

- **Modelo de Franjas Horarias Fijas:** Se optó por modelar `FranjaHoraria` como una entidad independiente con horarios estandarizados del centro (turno mañana y tarde) en lugar de permitir rangos de horas libres. Esto simplifica las validaciones de negocio y garantiza la integridad de los datos evitando solapamientos parciales.
- **Uso de `@RequestParam`:** Inicialmente se plantearon endpoints utilizando `@PathVariable`, pero se corrigieron a `@RequestParam` (`/aulas?ordenadores=true` y `/aulas?capacidad=20`) para cumplir estrictamente la especificación REST solicitada en el enunciado.
- **Notificaciones Toast:** En lugar de renderizar mensajes en bloques de texto simples, la IA sugirió implementar un sistema de Toasts animadas en el frontend para alertar al usuario sobre los resultados de las operaciones.

---

## ⚠️ Problemas Encontrados y Errores Corregidos

1. **Nombre de métodos en Spring Data JPA:**
   - *Problema:* El método `findByCapacidadMin` provocaba un error de inicialización en el contexto de Spring porque el atributo de la entidad se llamaba `capacidad`.
   - *Solución:* Se corrigió la firma del método a `findByCapacidadGreaterThan(int capacidad)` para hacer uso del vocabulario nativo de derivados de consulta de Spring Data.

2. **Validación de Disponibilidad:**
   - *Problema:* Métodos individuales como `existsByFechaReserva` o `existsByFranjaHoraria` bloqueaban fechas enteras o franjas globales en todas las aulas.
   - *Solución:* Se unificó en la consulta combinada `existsByAulaIdAndFechaReservaAndFranjaHorariaId` que valida la disponibilidad exacta para la tupla (Aula, Fecha, Franja).

3. **Bloqueo por CORS:**
   - *Problema:* Las peticiones Fetch del frontend al backend local (`http://localhost:8080`) eran bloqueadas por la política del navegador.
   - *Solución:* Se añadió la anotación `@CrossOrigin(origins = "*")` en los REST Controllers del backend.

4. **Conflictos en Git al publicar en GitHub:**
   - *Problema:* Error al hacer `git push` debido a la presencia de un commit previo con el archivo `README.md` remoto en el repositorio inicial.
   - *Solución:* Se ejecutó `git pull origin main --allow-unrelated-histories` para fusionar el historial remoto con el proyecto local.

---

## 🤖 Prompts Utilizados durante la Interacción con la IA

- *"No me hagas las actividad solo dime los atributos de las entidades."*
- *"franjaHoraria si seria una entidad?"*
- *"Esto es AulaController. Si lo ves bien hazme los demas controller (solo hazme los metodos del crud que hay en aula yo hago los demas)"*
- *"con solo poner el metodo en el repo ya vale. eso no lo entiendo"*
- *"Desarrollar un frontend con ayuda de herramientas de inteligencia artificial... sin frameworks."*

---

## 🚀 Instrucciones de Ejecución

1. Crear la base de datos `gestion_aulas` en phpMyAdmin / MySQL.
2. Configurar el archivo `application.properties` con las credenciales locales de la base de datos.
3. Ejecutar la aplicación Spring Boot (`src/main/java/.../GestionAulasApplication.java`). Se ejecutarán las consultas automáticas de creación de tablas e inserción de datos iniciales (`data.sql`).
4. Abrir la interfaz web navegando a la carpeta `/frontend/index.html` o a través del despliegue en **GitHub Pages**.
