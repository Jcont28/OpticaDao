async function pedir(url, metodo, datos) {
    const opciones = { method: metodo || 'GET', headers: {}, credentials: 'same-origin' };
    if (datos) {
        opciones.headers['Content-Type'] = 'application/json';
        opciones.body = JSON.stringify(datos);
    }
    const respuesta = await fetch(url, opciones);
    let json = null;
    try {
        json = await respuesta.json();
    } catch (e) {
        json = null;
    }
    if (respuesta.status === 401) {
        location.href = 'login.html';
        throw new Error('Debe iniciar sesion');
    }
    if (!respuesta.ok) {
        throw new Error(json && json.mensaje ? json.mensaje : 'Error del servidor (' + respuesta.status + ')');
    }
    return json;
}

async function verificarSesion(rol) {
    try {
        const sesion = await pedir('api/sesion');
        if (sesion.rol !== rol) {
            location.href = sesion.destino;
            return null;
        }
        const nombre = document.getElementById('nombreUsuario');
        if (nombre) {
            nombre.textContent = sesion.nombre;
        }
        return sesion;
    } catch (e) {
        location.href = 'login.html';
        return null;
    }
}

async function salir() {
    try {
        await pedir('api/logout', 'POST');
    } catch (e) {
    }
    location.href = 'login.html';
}

function dinero(valor) {
    return 'L. ' + Number(valor).toFixed(2);
}

function escapar(texto) {
    if (texto === null || texto === undefined) {
        return '';
    }
    return String(texto)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

function mostrarMensaje(id, texto, tipo) {
    const caja = document.getElementById(id);
    caja.className = 'alert alert-' + (tipo || 'info');
    caja.textContent = texto;
}

function llenarSelect(id, lista, textoFn) {
    const select = document.getElementById(id);
    let html = '<option value="">Seleccione</option>';
    for (const item of lista) {
        html += '<option value="' + item.id + '">' + escapar(textoFn(item)) + '</option>';
    }
    select.innerHTML = html;
}
