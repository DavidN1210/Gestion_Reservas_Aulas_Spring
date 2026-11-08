const API_BASE_URL = 'http://localhost:8080';

document.addEventListener('DOMContentLoaded', () => {
    cargarDesplegables();
    cargarAulas('/aulas');
    cargarReservas();

    // Eventos del Formulario de Reserva
    document.getElementById('form-reserva').addEventListener('submit', crearReserva);

    // Eventos de los Filtros de Aulas
    document.getElementById('btn-todas-aulas').addEventListener('click', () => cargarAulas('/aulas'));
    document.getElementById('btn-aulas-ordenadores').addEventListener('click', () => cargarAulas('/aulas?ordenadores=true'));
    document.getElementById('btn-filtrar-capacidad').addEventListener('click', filtrarPorCapacidad);

    // Evento de Reservas
    document.getElementById('btn-cargar-reservas').addEventListener('click', cargarReservas);
});

// Sistema de Notificaciones Toast
function mostrarToast(mensaje, tipo = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = mensaje;
    toast.className = `toast show ${tipo}`;
    setTimeout(() => { toast.className = 'toast'; }, 3500);
}

// Cargar opciones en los desplegables (Aulas, Profesores, Franjas)
async function cargarDesplegables() {
    try {
        const [resAulas, resProfesores, resFranjas] = await Promise.all([
            fetch(`${API_BASE_URL}/aulas`),
            fetch(`${API_BASE_URL}/profesores`),
            fetch(`${API_BASE_URL}/franjas-horarias`)
        ]);

        if(!resAulas.ok || !resProfesores.ok || !resFranjas.ok) throw new Error();

        const aulas = await resAulas.json();
        const profesores = await resProfesores.json();
        const franjas = await resFranjas.json();

        rellenarSelect('select-aula', aulas, a => `${a.nombre} (Cap: ${a.capacidad})`);
        rellenarSelect('select-profesor', profesores, p => `${p.nombre} ${p.apellidos}`);
        rellenarSelect('select-franja', franjas, f => `${f.descripcion || 'Franja'} [${f.horaInicio} - ${f.horaFin}]`);
    } catch (error) {
        mostrarToast('Error al conectar con el servidor. Comprueba que Spring Boot esté en ejecución.', 'error');
    }
}

function rellenarSelect(idSelect, datos, formatoTexto) {
    const select = document.getElementById(idSelect);
    select.innerHTML = '<option value="">Seleccione una opción...</option>';
    datos.forEach(item => {
        const option = document.createElement('option');
        option.value = item.id;
        option.textContent = formatoTexto(item);
        select.appendChild(option);
    });
}

// Cargar Aulas en la tabla
async function cargarAulas(endpoint) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`);
        const aulas = await response.json();
        const tbody = document.querySelector('#tabla-aulas tbody');
        tbody.innerHTML = '';

        aulas.forEach(aula => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><span class="badge">#${aula.id}</span></td>
                <td><strong>${aula.nombre}</strong></td>
                <td>${aula.capacidad} pax</td>
                <td>${aula.tieneOrdenadores ? '✅ Sí' : '❌ No'}</td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        mostrarToast('Error al cargar aulas', 'error');
    }
}

function filtrarPorCapacidad() {
    const capacidad = document.getElementById('input-capacidad').value;
    if (!capacidad) {
        mostrarToast('Introduce una capacidad', 'error');
        return;
    }
    cargarAulas(`/aulas?capacidad=${capacidad}`);
}

// Crear Reserva
async function crearReserva(event) {
    event.preventDefault();

    const reservaData = {
        aula: { id: parseInt(document.getElementById('select-aula').value) },
        profesor: { id: parseInt(document.getElementById('select-profesor').value) },
        franjaHoraria: { id: parseInt(document.getElementById('select-franja').value) },
        fechaReserva: document.getElementById('input-fecha').value
    };

    try {
        const response = await fetch(`${API_BASE_URL}/reservas`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(reservaData)
        });

        if (response.status === 201) {
            mostrarToast('Reserva creada con éxito', 'success');
            document.getElementById('form-reserva').reset();
            cargarReservas();
        } else {
            const errorMsg = await response.text();
            mostrarToast(errorMsg || 'No se pudo crear la reserva', 'error');
        }
    } catch (error) {
        mostrarToast('Error de conexión al crear reserva', 'error');
    }
}

// Cargar y Eliminar Reservas en la tabla
async function cargarReservas() {
    try {
        const response = await fetch(`${API_BASE_URL}/reservas`);
        const reservas = await response.json();
        const tbody = document.querySelector('#tabla-reservas tbody');
        tbody.innerHTML = '';

        reservas.forEach(reserva => {
            const fechaFormateada = new Date(reserva.fechaReserva).toLocaleDateString('es-ES');
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><strong>${fechaFormateada}</strong></td>
                <td>${reserva.aula ? reserva.aula.nombre : 'N/A'}</td>
                <td>${reserva.profesor ? `${reserva.profesor.nombre} ${reserva.profesor.apellidos}` : 'N/A'}</td>
                <td><span class="badge">${reserva.franjaHoraria ? reserva.franjaHoraria.descripcion : 'N/A'}</span></td>
                <td>
                    <button class="btn-danger" onclick="eliminarReserva(${reserva.id})">🗑️ Borrar</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        mostrarToast('Error al cargar reservas', 'error');
    }
}

async function eliminarReserva(id) {
    if (!confirm('¿Eliminar esta reserva?')) return;
    try {
        const response = await fetch(`${API_BASE_URL}/reservas/${id}`, { method: 'DELETE' });
        if (response.ok || response.status === 204) {
            mostrarToast('Reserva eliminada', 'success');
            cargarReservas();
        }
    } catch (error) {
        mostrarToast('Error al eliminar', 'error');
    }
}