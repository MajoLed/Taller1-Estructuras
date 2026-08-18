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

// Validación de correo inválido
cliente.setEmail("correo-invalido");

try {
    cliente.getEmail();
    assert false : "Debía lanzar IllegalArgumentException";
} catch (IllegalArgumentException e) {
    assert true;
}
        

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
        // assert ...

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
