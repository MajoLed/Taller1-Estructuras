package ADTs;

import java.time.LocalDate;

/*
 * Taller Práctico 01 - Sistema de Gestión de Taller de Reparación
 * Estructuras de Datos y Algoritmos - 2026-20
 *
 * María José Ledesma Cordoba - ID:000559241
 * Miguel Angel Puente Mejia - ID:000559418
 */

public class OrdenReparacion {

    private int idOrden;
    private String descripcion;
    private Estado estadoActual;
    private Servicio tipoServicio;
    private Cliente cliente; //dueño del vehículo
    private Mecanico mecanicoAsignado;
    private LocalDate fechaIngreso;
    private LocalDate fechaFinalizacion;

    public enum Estado {
    Recibida,
    Asignada,
    EnReparacion,
    Reparada,
    Entregada
}

public enum Servicio {
    Motor,
    Electricidad,
    Carrocería,
    Llantas
}
    private static int contador;

    public OrdenReparacion(
        String descripcion,
        Servicio tipoServicio,
        Cliente cliente,
        Mecanico mecanicoAsignado) {

    if (descripcion == null || descripcion.trim().isEmpty()) {
        throw new IllegalArgumentException(
                "La descripción no puede estar vacía"
        );
    }

    if (tipoServicio == null) {
        throw new IllegalArgumentException(
                "El tipo de servicio no puede ser null"
        );
    }

    if (cliente == null) {
        throw new IllegalArgumentException(
                "El cliente no puede ser null"
        );
    }

    idOrden = OrdenReparacion.contador++;
    this.descripcion = descripcion;
    this.estadoActual = Estado.Recibida;
    this.tipoServicio = tipoServicio;
    this.cliente = cliente;
    this.mecanicoAsignado = mecanicoAsignado;
    this.fechaIngreso = LocalDate.now();
        
}

    
    public void asignarMecanico(Mecanico mecanico) {

    if (mecanico == null) {
        throw new IllegalArgumentException(
                "El mecánico no puede ser null"
        );
    }

    if (estadoActual != Estado.Recibida) {
        throw new IllegalStateException(
                "Solo se puede asignar un mecánico a una orden recibida"
        );
    }

    if (!mecanico.estaDisponible()) {
        throw new IllegalStateException(
                "El mecánico está ocupado"
        );
    }

    mecanico.asignarOrden(this);
    this.mecanicoAsignado = mecanico;
    this.estadoActual = Estado.Asignada;
}

    public void cambiarEstado(Estado nuevoEstado) {

    if (nuevoEstado == null) {
        throw new IllegalArgumentException(
                "El nuevo estado no puede ser null"
        );
    }

    boolean transicionValida = false;

    switch (estadoActual) {

        case Recibida:
            transicionValida = nuevoEstado == Estado.Asignada;
            break;

        case Asignada:
            transicionValida = nuevoEstado == Estado.EnReparacion;
            break;

        case EnReparacion:
            transicionValida = nuevoEstado == Estado.Reparada;
            break;

        case Reparada:
            transicionValida = nuevoEstado == Estado.Entregada;
            break;

        case Entregada:
            transicionValida = false;
            break;
    }

    if (!transicionValida) {
        throw new IllegalStateException(
                "Transición inválida: " +
                estadoActual +
                " -> " +
                nuevoEstado
        );
    }

    estadoActual = nuevoEstado;
}


}
