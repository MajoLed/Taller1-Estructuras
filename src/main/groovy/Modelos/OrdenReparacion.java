package Modelos;

import java.time.LocalDate;

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
    private Servicio tipoServicio;
    private Cliente cliente; //dueño del vehículo
    private Mecanico mecanicoAsignado;
    private LocalDate fechaIngreso;
    private LocalDate fechaFinalizacion;

    private enum Estado {Recibida, Asignada, EnReparacion, Reparada, Entregada};
    private enum Servicio {Motor, Electricidad, Carrocería, Llantas};
    private static int contador;

    public OrdenReparacion(String descripcion, Servicio tipoServicio, Cliente cliente, Mecanico mecanicoAsignado) {
        idOrden = OrdenReparacion.contador++;
        this.descripcion = descripcion;
        this.estadoActual = Estado.Recibida;
        this.tipoServicio = tipoServicio;
        this.cliente = cliente;
        this.mecanicoAsignado = mecanicoAsignado;
        this.fechaIngreso = LocalDate.now();
    }

    public void asignarMecanico(Mecanico mecanico){

        if (mecanico != null && estadoActual == Estado.Recibida){
            estadoActual = Estado.Asignada;
        }
    }

    public void cambiarEstado(Estado nuevoEstado){

    }


}
