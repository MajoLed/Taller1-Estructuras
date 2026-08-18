package ADTs;

import java.time.Duration;
import java.time.LocalDateTime;

/*
 * Taller Práctico 01 - Sistema de Gestión de Taller de Reparación
 * Estructuras de Datos y Algoritmos - 2026-20
 *
 * María José Ledesma Cordoba - ID:000559241
 */

public class OrdenReparacion {

    private int idOrden;
    private String descripcion;
    private Estado estadoActual;
    Servicio tipoServicio; //No tiene metodo de acceso por encapsulacion, por definición igual es privada
    Cliente cliente; //dueño del vehículo
    private Mecanico mecanicoAsignado;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaFinalizacion;

    private static int contador;

    enum Estado {Recibida, Asignada, EnReparacion, Reparada, Entregada}
    enum Servicio {Motor, Electricidad, Frenos, Carroceria, Llantas}

    public OrdenReparacion(String descripcion, Servicio tipoServicio, Cliente cliente) {
        idOrden = contador++;
        this.descripcion = descripcion;
        this.estadoActual = Estado.Recibida;
        this.tipoServicio = tipoServicio;
        this.cliente = cliente;
        this.mecanicoAsignado = null;
        this.fechaIngreso = LocalDateTime.now();
        this.fechaFinalizacion = null;
    }

    // region Métodos

    public void asignarMecanico(Mecanico mecanico){

        if (mecanico == null) { //Verficando que pasen un mecanico válido
            throw new IllegalArgumentException("El mecánico no puede ser null");
        }

        if (estadoActual != Estado.Recibida) {
            throw new IllegalStateException(
                    "La orden no está en estado Recibida"
            );
        }

        //Como no se menciona que la orden debe mantener un mismo mecánico siempre, no se controla el caso

        mecanicoAsignado = mecanico;
        estadoActual = Estado.Asignada;

    }

    public void cambiarEstado(Estado nuevoEstado){

        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser null");
        }

        //Validación rápida en un booleano para cambiar de estados
        boolean validacionEstado = switch (estadoActual) {
            case Recibida -> nuevoEstado == Estado.Asignada;
            case Asignada -> nuevoEstado == Estado.EnReparacion;
            case EnReparacion -> nuevoEstado == Estado.Reparada;
            case Reparada -> nuevoEstado == Estado.Entregada;
            case Entregada -> false;
        };

        if (!validacionEstado) {
            throw new IllegalStateException("No se puede cambiar de " + estadoActual + " a " + nuevoEstado);
        }

        estadoActual = nuevoEstado;
    }

    public void finalizarReparacion() {
        if (estadoActual != Estado.EnReparacion ) {
            if (mecanicoAsignado == null )
                throw new IllegalStateException("La orden debe estar En reparacion para finalizarla");
        }

        estadoActual = Estado.Reparada;
        fechaFinalizacion = LocalDateTime.now();
    }

    public void entregar() {
        if (estadoActual != Estado.Reparada) {
            throw new IllegalStateException("La orden debe estar reparada para entregarla");
        }

        estadoActual = Estado.Entregada;
    }

    public Duration tiempoEnTaller() {
        // Se tienen los 2 casos de reparacion
        LocalDateTime fin = (fechaFinalizacion != null) ? fechaFinalizacion : LocalDateTime.now();
        return Duration.between(fechaIngreso, fin);
    }

    //endregion

    //region Getters
    public int getIdOrden() {
        return idOrden;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Estado getEstadoActual() {
        return estadoActual;
    }

    public Servicio getTipoServicio() {
        return tipoServicio;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Mecanico getMecanicoAsignado() {
        return mecanicoAsignado;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }
    //endregion

    //Heredable
    @Override
    public String toString() {
        return "OrdenReparacion{" +
                "idOrden=" + idOrden +
                ", descripcion='" + descripcion + '\'' +
                ", estadoActual=" + estadoActual +
                ", tipoServicio=" + tipoServicio +
                ", cliente=" + cliente +
                ", mecanicoAsignado=" + mecanicoAsignado +
                ", fechaIngreso=" + fechaIngreso +
                ", fechaFinalizacion=" + fechaFinalizacion +
                '}';
    }
}
