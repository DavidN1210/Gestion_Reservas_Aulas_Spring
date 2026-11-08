# Instrucciones para Agentes de Inteligencia Artificial (AGENTS.md)

Este documento detalla las directrices y contexto proporcionados a los asistentes de IA durante el desarrollo del backend (Spring Boot) y frontend (HTML/CSS/JS) de la aplicación de gestión de reservas de aulas.

## 1. Contexto del Proyecto
- **Curso:** 2.º Desarrollo de Aplicaciones Multiplataforma (DAM).
- **Módulo:** Acceso a Datos.
- **Tecnologías Backend:** Java 17+, Spring Boot, JPA / Hibernate, MySQL, Lombok.
- **Tecnologías Frontend:** HTML5, CSS3, JavaScript ES6 (sin frameworks ni librerías externas).

## 2. Reglas Generales de Código
- Mantener la arquitectura en capas estándar de Spring Boot: `Entity`, `Repository`, `Controller`.
- Utilizar Lombok (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`) para reducir código repetitivo.
- Implementar respuestas HTTP semánticas y adecuadas en los controladores (`200 OK`, `201 Created`, `204 No Content`, `400 Bad Request`, `404 Not Found`, `409 Conflict`).
- Las franjas horarias deben tratarse como entidades fijas e indivisibles para evitar solapamientos ambiguos.

## 3. Especificaciones de Negocio
- La fecha de creación de una reserva debe generarse automáticamente al persistir mediante `@PrePersist`.
- No permitir reservas para fechas anteriores a la actual (`LocalDate.now()`).
- Evitar reservas duplicadas comprobando la disponibilidad única de la combinación (`aulaId`, `fechaReserva`, `franjaHorariaId`).
- Permitir la consulta de aulas filtrando por disponibilidad de ordenadores (`?ordenadores=true`) y capacidad mínima (`?capacidad=X`) mediante `@RequestParam`.

## 4. Estilo del Frontend
- Interfaz moderna basada en componentes tipo "Card" y diseño Grid/Flexbox.
- Notificaciones de error y éxito no invasivas mediante componentes Toast en lugar de `alert()` o texto estático.
- Habilitar soporte CORS (`@CrossOrigin`) en los controladores de Spring Boot para permitir la comunicación cliente-servidor en entorno local.
