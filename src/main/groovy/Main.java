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
        // assert ...

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
