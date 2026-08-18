package ADTs;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/*
 * Taller Práctico 01 - Sistema de Gestión de Taller de Reparación
 * Estructuras de Datos y Algoritmos - 2026-20
 *
 * María José Ledesma Cordoba - ID:000559241
 * Miguel Angel Puente Mejia - ID:000559418
 */

/**
 * ADT TallerSystem: coordina la gestión de órdenes, mecánicos y tipos de
 * servicio de AutoFix Ltda.
 *
 * Estructuras usadas y por qué:
 * - Queue<OrdenReparacion> ordenesPendientes: el requisito de negocio 1
 *   exige atención estrictamente FIFO, así que la cola es la estructura
 *   natural (encolar/desencolar en O(1)).
 * - List<OrdenReparacion> historial: cada orden finalizada se agrega al
 *   inicio, por lo que iterar la lista entrega "más reciente primero"
 *   (requisito de negocio 5) sin ordenar por fecha en cada consulta.
 * - Bag<OrdenReparacion> todasLasOrdenes / Bag<Mecanico> mecanicos /
 *   Bag<OrdenReparacion.Servicio> tiposServicio: en los tres casos solo
 *   importa poder agregar, verificar pertenencia y recorrer/filtrar; nunca
 *   hay que "sacar" un elemento por un extremo en particular como en la
 *   Queue, así que la Bag (sin orden garantizado) es suficiente y más simple.
 */
public class TallerSystem {

    private Queue<OrdenReparacion> ordenesPendientes;
    private Bag<OrdenReparacion> todasLasOrdenes;
    private List<OrdenReparacion> historial;
    private Bag<Mecanico> mecanicos;
    private Bag<OrdenReparacion.Servicio> tiposServicio;

    public TallerSystem() {
        this.ordenesPendientes = new Queue<>();
        this.todasLasOrdenes = new Bag<>();
        this.historial = new List<>();
        this.mecanicos = new Bag<>();
        this.tiposServicio = new Bag<>();
    }

    // region Gestión de órdenes

    /**
     * Crea una nueva orden y la registra en el sistema y en la cola de
     * pendientes. Precondición: tipoServicio debe existir en tiposServicio.
     */
    public OrdenReparacion crearOrden(String descripcion, OrdenReparacion.Servicio tipoServicio, Cliente cliente) {
        if (tipoServicio == null || !tiposServicio.contiene(tipoServicio)) {
            throw new IllegalArgumentException(
                    "El tipo de servicio '" + tipoServicio + "' no está registrado en el sistema"
            );
        }

        OrdenReparacion nuevaOrden = new OrdenReparacion(descripcion, tipoServicio, cliente);
        todasLasOrdenes.agregar(nuevaOrden);
        ordenesPendientes.encolar(nuevaOrden);
        return nuevaOrden;
    }

    /**
     * Toma la siguiente orden de la cola FIFO y la asigna al primer mecánico
     * disponible con la especialidad requerida. Si no hay ninguno
     * disponible, la orden vuelve a la cola (al final, dada la API de
     * Queue) y el método retorna null.
     */
    public OrdenReparacion asignarOrdenAutomatica() {
        if (ordenesPendientes.esVacia()) {
            throw new NoSuchElementException("No hay órdenes pendientes por asignar");
        }

        OrdenReparacion siguiente = ordenesPendientes.desencolar();
        Mecanico elegido = buscarMecanicoDisponible(siguiente.getTipoServicio());

        if (elegido == null) {
            ordenesPendientes.encolar(siguiente);
            return null;
        }

        vincularOrdenYMecanico(siguiente, elegido);
        return siguiente;
    }

    /** Asigna una orden a un mecánico específico, sin pasar por la cola. */
    public void asignarOrdenManual(OrdenReparacion orden, Mecanico mecanico) {
        if (orden == null || mecanico == null) {
            throw new IllegalArgumentException("La orden y el mecánico no pueden ser null");
        }

        if (!mecanico.estaDisponible()) {
            throw new IllegalStateException("El mecánico no está disponible");
        }

        vincularOrdenYMecanico(orden, mecanico);
    }

    public void cambiarEstadoOrden(int ordenId, OrdenReparacion.Estado nuevoEstado) {
        OrdenReparacion orden = buscarOrdenPorId(ordenId);
        orden.cambiarEstado(nuevoEstado);
    }

    /**
     * Marca la orden como Reparada, libera al mecánico y la agrega al
     * inicio del historial.
     */
    public void finalizarOrden(int ordenId) {
        OrdenReparacion orden = buscarOrdenPorId(ordenId);
        orden.finalizarReparacion();

        Mecanico mecanico = orden.getMecanicoAsignado();
        if (mecanico != null) {
            mecanico.completarOrden();
        }

        historial.agregarInicio(orden);
    }

    public void entregarOrden(int ordenId) {
        OrdenReparacion orden = buscarOrdenPorId(ordenId);
        orden.entregar();
    }

    private Mecanico buscarMecanicoDisponible(OrdenReparacion.Servicio servicio) {
        for (Mecanico mecanico : mecanicos) {
            // Especialidad (Mecanico) y Servicio (OrdenReparacion) son enums
            // distintos; se comparan por nombre porque comparten los mismos
            // valores textuales para las especialidades que sí cubren.
            if (mecanico.estaDisponible() && mecanico.getEspecialidad().name().equals(servicio.name())) {
                return mecanico;
            }
        }
        return null;
    }

    private void vincularOrdenYMecanico(OrdenReparacion orden, Mecanico mecanico) {
        orden.asignarMecanico(mecanico); // esto ya enlaza también al mecánico con la orden
    }

    private OrdenReparacion buscarOrdenPorId(int ordenId) {
        for (OrdenReparacion orden : todasLasOrdenes) {
            if (orden.getIdOrden() == ordenId) {
                return orden;
            }
        }
        throw new NoSuchElementException("No existe una orden con id " + ordenId);
    }

    // endregion

    // region Gestión de mecánicos y tipos de servicio

    public void agregarMecanico(Mecanico mecanico) {
        if (mecanico == null) {
            throw new IllegalArgumentException("El mecánico no puede ser null");
        }
        mecanicos.agregar(mecanico);
    }

    public void agregarTipoServicio(OrdenReparacion.Servicio tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de servicio no puede ser null");
        }
        tiposServicio.agregar(tipo);
    }

    // endregion

    // region Consultas y reportes (iteradores)

    /** Órdenes pendientes en orden FIFO real (solo lectura). */
    public Iterator<OrdenReparacion> obtenerOrdenesPendientes() {
        return ordenesPendientes.iterator();
    }

    /** Historial de finalizadas, de la más reciente a la más antigua. */
    public Iterator<OrdenReparacion> obtenerHistorial() {
        return historial.iterator();
    }

    public Iterator<OrdenReparacion> obtenerOrdenesPorEstado(OrdenReparacion.Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser null");
        }
        return todasLasOrdenes.iteradorFiltrado(orden -> orden.getEstadoActual() == estado);
    }

    public Iterator<OrdenReparacion> obtenerOrdenesPorTipo(OrdenReparacion.Servicio tipoServicio) {
        if (tipoServicio == null) {
            throw new IllegalArgumentException("El tipo de servicio no puede ser null");
        }
        return todasLasOrdenes.iteradorFiltrado(orden -> orden.getTipoServicio() == tipoServicio);
    }

    public OrdenReparacion obtenerOrdenDeMecanico(Mecanico mecanico) {
        if (mecanico == null) {
            throw new IllegalArgumentException("El mecánico no puede ser null");
        }
        return mecanico.getOrdenAsignada();
    }

    /** Cantidad de órdenes registradas por cada estado. */
    public Map<OrdenReparacion.Estado, Integer> generarEstadisticas() {
        Map<OrdenReparacion.Estado, Integer> estadisticas = new EnumMap<>(OrdenReparacion.Estado.class);
        for (OrdenReparacion.Estado estado : OrdenReparacion.Estado.values()) {
            estadisticas.put(estado, 0);
        }
        for (OrdenReparacion orden : todasLasOrdenes) {
            estadisticas.merge(orden.getEstadoActual(), 1, Integer::sum);
        }
        return estadisticas;
    }

    public int totalOrdenes() {
        return todasLasOrdenes.tamano();
    }

    // endregion
}
