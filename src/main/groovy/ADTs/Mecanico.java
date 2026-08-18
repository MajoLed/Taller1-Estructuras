package ADTs;

/*
 * Taller Práctico 01 - Sistema de Gestión de Taller de Reparación
 * Estructuras de Datos y Algoritmos - 2026-20
 *
 * María José Ledesma Cordoba - ID:000559241
 */

public class Mecanico {

    public enum Especialidad {Motor, Frenos, Electricidad, Carroceria, Bujías}

    private int id;
    private String nombre; //nombre completo
    private Especialidad especialidad;
    private OrdenReparacion ordenAsignada;
    private boolean disponible;

    private static int contador; //para asignar en constructor

    public Mecanico(String nombre, Especialidad especialidad) {
        this.id = Mecanico.contador++;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.ordenAsignada = null;
        this.disponible = true;
    }

    //Métodos

    public void asignarOrden(OrdenReparacion orden){

        if (orden == null) //Verifico que la orden que mehayn pasado sea válida
            throw new IllegalArgumentException("La orden está vacía");

        if ( ordenAsignada == null && estaDisponible() ){
            ordenAsignada = orden;
            disponible = false;
        }
        else {
            throw new IllegalArgumentException("El mecánico está ocupado");
        }

    }

    public void completarOrden(){
        ordenAsignada = null;
        this.disponible = true;
    }

    public boolean estaDisponible() {
        return  disponible; //True si está disponible, false si no.
    }

    public void marcarDisponibilidad(boolean disponibilidad){

        if (ordenAsignada != null && disponibilidad) //Exepción para  caso de que pongan disponibilidad y tenga caso
        {
            disponible = false;
            System.out.println("Se no se cambió la disponibilidad por el estado de la orden");
        }
        else {
            disponible = disponibilidad;
        }

    }

    public boolean isDisponible() {
        return disponible;
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public OrdenReparacion getOrdenAsignada() {
        return ordenAsignada;
    }

    @Override
    public String toString() {
        return "Mecanico{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", especialidad=" + especialidad +
                ", ordenAsignada=" + ordenAsignada +
                ", disponible=" + disponible +
                '}';
    }
}
