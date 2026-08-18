import ADTs.*;
import java.util.Iterator;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

// Pruebas de Cliente

Cliente cliente = new Cliente(
        "Juan Pérez",
        "3001234567",
        "juan@gmail.com",
        "ABC123"
);

// Constructor y getters
assert cliente.getNombre().equals("Juan Pérez");
assert cliente.getTelefono().equals("3001234567");
assert cliente.getEmail().equals("juan@gmail.com");
assert cliente.getPlacaVehiculo().equals("ABC123");

// ID asignado
assert cliente.getId() >= 0;

// Setters
cliente.setNombre("Pedro Gómez");
assert cliente.getNombre().equals("Pedro Gómez");

cliente.setEmail("pedro@gmail.com");
assert cliente.getEmail().equals("pedro@gmail.com");

cliente.setTelefono("3109876543");
assert cliente.getTelefono().equals("3109876543");

cliente.setPlacaVehiculo("XYZ789");
assert cliente.getPlacaVehiculo().equals("XYZ789");

// Validación de nombre vacío
try {
    cliente.setNombre("");
    assert false : "Debía lanzar IllegalArgumentException";
} catch (IllegalArgumentException e) {
    assert true;
}

// Validación de nombre null
try {
    cliente.setNombre(null);
    assert false : "Debía lanzar IllegalArgumentException";
} catch (IllegalArgumentException e) {
    assert true;
}

// Validación de correo inválido
try {
    cliente.setEmail("correo-invalido");
    cliente.getEmail();
    assert false : "Debía lanzar IllegalArgumentException";
} catch (IllegalArgumentException e) {
    assert true;
}

// Restaurar correo válido para continuar las pruebas
cliente.setEmail("pedro@gmail.com");
assert cliente.getEmail().equals("pedro@gmail.com");

        // Pruebas de Mecanico

Mecanico mecanico = new Mecanico(
        "Carlos Pérez",
        Mecanico.Especialidad.Motor
);

// Constructor y getters
assert mecanico.getNombre().equals("Carlos Pérez");
assert mecanico.getEspecialidad() == Mecanico.Especialidad.Motor;
assert mecanico.getId() >= 0;

// El mecánico inicia disponible
assert mecanico.estaDisponible();
assert mecanico.getOrdenAsignada() == null;

// Crear una orden para probar asignación
Cliente cliente = new Cliente(
        "Juan Pérez",
        "3001234567",
        "juan@gmail.com",
        "ABC123"
);

OrdenReparacion orden = new OrdenReparacion(
        "Cambio de aceite",
        OrdenReparacion.Servicio.Motor,
        cliente,
        null
);

// Asignar orden
mecanico.asignarOrden(orden);

assert mecanico.getOrdenAsignada() == orden;
assert !mecanico.estaDisponible();

// No puede asignar otra orden mientras está ocupado
try {
    mecanico.asignarOrden(orden);
    assert false : "Debía lanzar IllegalStateException";
} catch (IllegalStateException e) {
    assert true;
}

// No puede marcarse disponible mientras tiene una orden
try {
    mecanico.marcarDisponibilidad(true);
    assert false : "Debía lanzar IllegalStateException";
} catch (IllegalStateException e) {
    assert true;
}

// Completar orden
mecanico.completarOrden();

assert mecanico.getOrdenAsignada() == null;
assert mecanico.estaDisponible();

// No puede completar una orden si no tiene ninguna
try {
    mecanico.completarOrden();
    assert false : "Debía lanzar IllegalStateException";
} catch (IllegalStateException e) {
    assert true;
}

// No puede asignar null
try {
    mecanico.asignarOrden(null);
    assert false : "Debía lanzar IllegalArgumentException";
} catch (IllegalArgumentException e) {
    assert true;
}

// Pruebas de OrdenReparacion

Cliente clienteOrden = new Cliente(
        "Ana López",
        "3001112233",
        "ana@gmail.com",
        "ABC123"
);

Mecanico mecanicoOrden = new Mecanico(
        "Carlos Gómez",
        Mecanico.Especialidad.Motor
);

OrdenReparacion orden = new OrdenReparacion(
        "Problema en el motor",
        OrdenReparacion.Servicio.Motor,
        clienteOrden,
        null
);

// La orden debe iniciar en estado Recibida.
// Se comprueba intentando asignar un mecánico.
orden.asignarMecanico(mecanicoOrden);

assert mecanicoOrden.getOrdenAsignada() == orden;
assert !mecanicoOrden.estaDisponible();

// Una orden ya asignada no puede recibir otro mecánico
Mecanico otroMecanico = new Mecanico(
        "Pedro Ruiz",
        Mecanico.Especialidad.Electricidad
);

try {
    orden.asignarMecanico(otroMecanico);
    assert false : "No se debería poder asignar otro mecánico";
} catch (IllegalStateException e) {
    assert true;
}

// No se puede saltar de Asignada directamente a Reparada
try {
    orden.cambiarEstado(OrdenReparacion.Estado.Reparada);
    assert false : "No se debería permitir saltar estados";
} catch (IllegalStateException e) {
    assert true;
}

// Asignada -> EnReparacion
orden.cambiarEstado(OrdenReparacion.Estado.EnReparacion);

// EnReparacion -> Reparada
orden.cambiarEstado(OrdenReparacion.Estado.Reparada);

// Reparada -> Entregada
orden.cambiarEstado(OrdenReparacion.Estado.Entregada);

// Una orden entregada no puede cambiar nuevamente de estado
try {
    orden.cambiarEstado(OrdenReparacion.Estado.Recibida);
    assert false : "Una orden entregada no puede cambiar de estado";
} catch (IllegalStateException e) {
    assert true;
}

// No se puede asignar un mecánico null
OrdenReparacion otraOrden = new OrdenReparacion(
        "Cambio de llantas",
        OrdenReparacion.Servicio.Llantas,
        clienteOrden,
        null
);

try {
    otraOrden.asignarMecanico(null);
    assert false : "No se debería permitir un mecánico null";
} catch (IllegalArgumentException e) {
    assert true;
}

// No se permite crear una orden con descripción vacía
try {
    new OrdenReparacion(
            "",
            OrdenReparacion.Servicio.Motor,
            clienteOrden,
            null
    );
    assert false : "No se debería permitir una descripción vacía";
} catch (IllegalArgumentException e) {
    assert true;
}

// No se permite crear una orden con servicio null
try {
    new OrdenReparacion(
            "Revisión",
            null,
            clienteOrden,
            null
    );
    assert false : "No se debería permitir un servicio null";
} catch (IllegalArgumentException e) {
    assert true;
}

// No se permite crear una orden con cliente null
try {
    new OrdenReparacion(
            "Revisión",
            OrdenReparacion.Servicio.Motor,
            null,
            null
    );
    assert false : "No se debería permitir un cliente null";
} catch (IllegalArgumentException e) {
    assert true;
}

        // ===================== Pruebas de TallerSystem =====================

        TallerSystem taller = new TallerSystem();

        // Setup: tipos de servicio y mecánicos predefinidos
        taller.agregarTipoServicio(OrdenReparacion.Servicio.Motor);
        taller.agregarTipoServicio(OrdenReparacion.Servicio.Frenos);
        taller.agregarTipoServicio(OrdenReparacion.Servicio.Electricidad);

        Mecanico mecMotor = new Mecanico("Luis Torres", Mecanico.Especialidad.Motor);
        Mecanico mecFrenos = new Mecanico("Ana Ríos", Mecanico.Especialidad.Frenos);
        taller.agregarMecanico(mecMotor);
        taller.agregarMecanico(mecFrenos);

        Cliente clienteTaller = new Cliente("Sofía Rendón", "3005556677", "sofia@gmail.com", "TAL001");

        // No se puede crear una orden con un tipo de servicio no registrado
        try {
            taller.crearOrden("Cambio de llantas", OrdenReparacion.Servicio.Llantas, clienteTaller);
            assert false : "Debía lanzar IllegalArgumentException por tipo de servicio no registrado";
        } catch (IllegalArgumentException e) {
            assert true;
        }

        // Crear orden válida: debe quedar en 'todasLasOrdenes' y en la cola de pendientes
        OrdenReparacion ordenMotor = taller.crearOrden("Ruido en el motor", OrdenReparacion.Servicio.Motor, clienteTaller);
        assert ordenMotor.getEstadoActual() == OrdenReparacion.Estado.Recibida;
        assert taller.totalOrdenes() == 1;

        Iterator<OrdenReparacion> pendientesIniciales = taller.obtenerOrdenesPendientes();
        assert pendientesIniciales.hasNext();
        assert pendientesIniciales.next() == ordenMotor;

        // Asignación automática: debe tomar la orden de la cola y asignarla al mecánico de Motor
        OrdenReparacion asignada = taller.asignarOrdenAutomatica();
        assert asignada == ordenMotor;
        assert asignada.getEstadoActual() == OrdenReparacion.Estado.Asignada;
        assert asignada.getMecanicoAsignado() == mecMotor;
        assert !mecMotor.estaDisponible();
        assert !taller.obtenerOrdenesPendientes().hasNext(); // la cola quedó vacía

        // Si no hay mecánico disponible para el tipo de servicio, la orden vuelve a la cola
        OrdenReparacion ordenFrenos1 = taller.crearOrden("Pastillas gastadas", OrdenReparacion.Servicio.Frenos, clienteTaller);
        taller.asignarOrdenAutomatica(); // ocupa al único mecánico de Frenos
        OrdenReparacion ordenFrenos2 = taller.crearOrden("Disco desgastado", OrdenReparacion.Servicio.Frenos, clienteTaller);
        OrdenReparacion resultadoSinMecanico = taller.asignarOrdenAutomatica();
        assert resultadoSinMecanico == null; // no había mecánico de Frenos disponible
        assert ordenFrenos2.getEstadoActual() == OrdenReparacion.Estado.Recibida; // sigue pendiente

        // asignarOrdenAutomatica() sobre cola vacía debe lanzar excepción
        TallerSystem tallerVacio = new TallerSystem();
        try {
            tallerVacio.asignarOrdenAutomatica();
            assert false : "Debía lanzar NoSuchElementException con la cola vacía";
        } catch (java.util.NoSuchElementException e) {
            assert true;
        }

        // Flujo completo: EnReparacion -> finalizar -> entregar, y verificar historial
        taller.cambiarEstadoOrden(ordenMotor.getIdOrden(), OrdenReparacion.Estado.EnReparacion);
        taller.finalizarOrden(ordenMotor.getIdOrden());
        assert ordenMotor.getEstadoActual() == OrdenReparacion.Estado.Reparada;
        assert mecMotor.estaDisponible(); // finalizarOrden debe liberar al mecánico

        taller.entregarOrden(ordenMotor.getIdOrden());
        assert ordenMotor.getEstadoActual() == OrdenReparacion.Estado.Entregada;

        Iterator<OrdenReparacion> historialTaller = taller.obtenerHistorial();
        assert historialTaller.hasNext();
        assert historialTaller.next() == ordenMotor; // única orden finalizada hasta ahora

        // No se puede finalizar una orden que no está EnReparacion (aunque tenga mecánico)
        try {
            taller.finalizarOrden(ordenFrenos1.getIdOrden()); // está en 'Asignada', no en 'EnReparacion'
            assert false : "Debía lanzar IllegalStateException: la orden no está EnReparacion";
        } catch (IllegalStateException e) {
            assert true;
        }

        // Operar sobre un id de orden inexistente debe lanzar excepción
        try {
            taller.cambiarEstadoOrden(999999, OrdenReparacion.Estado.Asignada);
            assert false : "Debía lanzar NoSuchElementException por id inexistente";
        } catch (java.util.NoSuchElementException e) {
            assert true;
        }

        // asignarOrdenManual: no se puede asignar a un mecánico ocupado
        try {
            taller.asignarOrdenManual(ordenFrenos2, mecFrenos); // mecFrenos sigue ocupado con ordenFrenos1
            assert false : "Debía lanzar IllegalStateException: mecánico ocupado";
        } catch (IllegalStateException e) {
            assert true;
        }

        // obtenerOrdenesPorEstado / obtenerOrdenesPorTipo (usan Bag.iteradorFiltrado)
        int contadorRecibidas = 0;
        Iterator<OrdenReparacion> recibidas = taller.obtenerOrdenesPorEstado(OrdenReparacion.Estado.Recibida);
        while (recibidas.hasNext()) {
            assert recibidas.next().getEstadoActual() == OrdenReparacion.Estado.Recibida;
            contadorRecibidas++;
        }
        assert contadorRecibidas == 1; // solo ordenFrenos2

        int contadorFrenos = 0;
        Iterator<OrdenReparacion> porTipoFrenos = taller.obtenerOrdenesPorTipo(OrdenReparacion.Servicio.Frenos);
        while (porTipoFrenos.hasNext()) {
            assert porTipoFrenos.next().getTipoServicio() == OrdenReparacion.Servicio.Frenos;
            contadorFrenos++;
        }
        assert contadorFrenos == 2; // ordenFrenos1 y ordenFrenos2

        // obtenerOrdenDeMecanico
        assert taller.obtenerOrdenDeMecanico(mecFrenos) == ordenFrenos1;
        assert taller.obtenerOrdenDeMecanico(mecMotor) == null; // ya fue liberado

        // generarEstadisticas: total de órdenes por estado debe sumar el total de órdenes
        Map<OrdenReparacion.Estado, Integer> estadisticas = taller.generarEstadisticas();
        int sumaEstadisticas = 0;
        for (int cantidad : estadisticas.values()) {
            sumaEstadisticas += cantidad;
        }
        assert sumaEstadisticas == taller.totalOrdenes();
        assert estadisticas.get(OrdenReparacion.Estado.Entregada) == 1; // ordenMotor

        // No se puede crear un mecánico/tipo null
        try {
            taller.agregarMecanico(null);
            assert false : "Debía lanzar IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert true;
        }

        // ===================== Pruebas de Queue =====================

        Queue<String> cola = new Queue<>();

        assert cola.esVacia();
        assert cola.tamano() == 0;

        cola.encolar("Orden-1");
        cola.encolar("Orden-2");
        cola.encolar("Orden-3");

        assert cola.tamano() == 3;
        assert !cola.esVacia();
        assert cola.verFrente().equals("Orden-1"); // FIFO: la primera en entrar es la primera al frente

        assert cola.desencolar().equals("Orden-1");
        assert cola.tamano() == 2;
        assert cola.verFrente().equals("Orden-2");

        // No se puede encolar null
        try {
            cola.encolar(null);
            assert false : "Debía lanzar IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert true;
        }

        // Vaciar la cola y validar excepción al desencolar/verFrente sobre cola vacía
        cola.desencolar();
        cola.desencolar();
        assert cola.esVacia();

        try {
            cola.desencolar();
            assert false : "Debía lanzar NoSuchElementException";
        } catch (java.util.NoSuchElementException e) {
            assert true;
        }

        try {
            cola.verFrente();
            assert false : "Debía lanzar NoSuchElementException";
        } catch (java.util.NoSuchElementException e) {
            assert true;
        }

        // ===================== Pruebas de List =====================

        List<String> historialLista = new List<>();

        assert historialLista.estaVacia();

        // Simulamos 3 órdenes finalizándose en orden cronológico
        historialLista.agregarInicio("Orden-A (finalizada primero)");
        historialLista.agregarInicio("Orden-B (finalizada segundo)");
        historialLista.agregarInicio("Orden-C (finalizada tercero)");

        assert historialLista.tamano() == 3;
        // La más reciente (Orden-C) debe quedar de primera en el historial
        assert historialLista.obtener(0).equals("Orden-C (finalizada tercero)");
        assert historialLista.obtener(2).equals("Orden-A (finalizada primero)");

        try {
            historialLista.obtener(10);
            assert false : "Debía lanzar IndexOutOfBoundsException";
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        try {
            historialLista.agregarInicio(null);
            assert false : "Debía lanzar IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert true;
        }

        List<Integer> listaOrdenAlFinal = new List<>();
        listaOrdenAlFinal.agregarFinal(1);
        listaOrdenAlFinal.agregarFinal(2);
        listaOrdenAlFinal.agregarFinal(3);
        assert listaOrdenAlFinal.obtener(0) == 1 && listaOrdenAlFinal.obtener(2) == 3;

        // ===================== Pruebas de Bag =====================

        Bag<String> tiposServicioBag = new Bag<>();

        assert tiposServicioBag.estaVacia();

        tiposServicioBag.agregar("Motor");
        tiposServicioBag.agregar("Frenos");
        tiposServicioBag.agregar("Electricidad");

        assert tiposServicioBag.tamano() == 3;
        assert tiposServicioBag.contiene("Frenos");
        assert !tiposServicioBag.contiene("Suspensión");

        try {
            tiposServicioBag.agregar(null);
            assert false : "Debía lanzar IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert true;
        }

        // Forzar redimensionamiento interno del arreglo (capacidad inicial = 10)
        Bag<Integer> bolsaGrande = new Bag<>();
        for (int i = 0; i < 25; i++) {
            bolsaGrande.agregar(i);
        }
        assert bolsaGrande.tamano() == 25;
        assert bolsaGrande.contiene(24);

         // ===================== Pruebas de los iteradores =====================

        // Iterador de Queue: debe respetar el orden FIFO y no vaciar la cola (solo lectura)
        Queue<Integer> colaIter = new Queue<>();
        colaIter.encolar(10);
        colaIter.encolar(20);
        colaIter.encolar(30);

        int sumaEsperada = 0;
        int cantidadRecorrida = 0;
        for (int valor : colaIter) {
            sumaEsperada += valor;
            cantidadRecorrida++;
        }
        assert sumaEsperada == 60;
        assert cantidadRecorrida == 3;
        assert colaIter.tamano() == 3; // el iterador no debe alterar la cola

        // Iterador de List: debe recorrer el historial de más reciente a más antigua
        StringBuilder ordenRecorrido = new StringBuilder();
        for (String item : historialLista) {
            ordenRecorrido.append(item.charAt(6)); // toma la letra (A, B o C)
        }
        assert ordenRecorrido.toString().equals("CBA");

        // Iterador filtrado de Bag: simula obtenerOrdenesPorEstado()/obtenerOrdenesPorTipo()
        Bag<Integer> numeros = new Bag<>();
        for (int i = 1; i <= 10; i++) {
            numeros.agregar(i);
        }

        Iterator<Integer> pares = numeros.iteradorFiltrado(n -> n % 2 == 0);
        int contadorPares = 0;
        int sumaPares = 0;
        while (pares.hasNext()) {
            int p = pares.next();
            assert p % 2 == 0;
            sumaPares += p;
            contadorPares++;
        }
        assert contadorPares == 5;
        assert sumaPares == 30; // 2+4+6+8+10

        // Un filtro que no matchea nada debe dar un iterador vacío, no un error
        Iterator<Integer> ninguno = numeros.iteradorFiltrado(n -> n > 1000);
        assert !ninguno.hasNext();

    }
}
