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

        // Pruebas de TallerSystem
        // assert ...

        // Pruebas de Queue
        // assert ...

        // Pruebas de List
        // assert ...

        // Pruebas de Bag
        // assert ...

        // Pruebas de los iteradores
        // assert ...

    }
}
