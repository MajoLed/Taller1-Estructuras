package ADTs;

import Estructuras.Bag;
import Estructuras.List;
import Estructuras.Queue;

import java.time.Duration;
import java.util.Iterator;
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
    private List<OrdenReparacion> historial; // más reciente al frente
    private Bag<Mecanico> mecanicos;
    private Bag<OrdenReparacion.Servicio> tiposServicio;
    private int proximoOrdenId;

    public TallerSystem() {
        this.ordenesPendientes = new Queue<>();
        this.todasLasOrdenes = new Bag<>();
        this.historial = new List<>();
        this.mecanicos = new Bag<>();
        this.tiposServicio = new Bag<>();
        this.proximoOrdenId = 0;
    }

    // region Gestión de órdenes

    public OrdenReparacion crearOrden(String descripcion, OrdenReparacion.Servicio tipoServicio, Cliente cliente) {
        if (tipoServicio == null || !tiposServicio.contiene(tipoServicio)) {
            throw new IllegalArgumentException(
                    "El tipo de servicio '" + tipoServicio + "' no está registrado en el sistema"
            );
        }

        int id = proximoOrdenId++;
        OrdenReparacion nuevaOrden = new OrdenReparacion(id, descripcion, tipoServicio, cliente);
        todasLasOrdenes.agregar(nuevaOrden);
        ordenesPendientes.encolar(nuevaOrden);
        return nuevaOrden;
    }

    public OrdenReparacion asignarOrdenAutomatica() {
        if (ordenesPendientes.esVacia()) {
            throw new NoSuchElementException("No hay órdenes pendientes por asignar");
        }

        OrdenReparacion siguiente = ordenesPendientes.verFrente();
        while (siguiente.getEstadoActual() != OrdenReparacion.Estado.Recibida) {
            ordenesPendientes.desencolar();
            if (ordenesPendientes.esVacia()) {
                throw new NoSuchElementException("No hay órdenes pendientes por asignar");
            }
            siguiente = ordenesPendientes.verFrente();
        }

        Mecanico elegido = buscarMecanicoDisponible(siguiente.getTipoServicio());

        if (elegido == null) {
            return null; // permanece en la cola, al frente
        }

        ordenesPendientes.desencolar();
        vincularOrdenYMecanico(siguiente, elegido);
        return siguiente;
    }

    public void asignarOrdenManual(OrdenReparacion orden, Mecanico mecanico) {
        if (orden == null || mecanico == null) {
            throw new IllegalArgumentException("La orden y el mecánico no pueden ser null");
        }
        if (!todasLasOrdenes.contiene(orden)) {
            throw new IllegalArgumentException("La orden no pertenece a este taller");
        }
        if (!mecanicos.contiene(mecanico)) {
            throw new IllegalArgumentException("El mecánico no pertenece a este taller");
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
            if (mecanico.estaDisponible() && mecanico.getEspecialidad().name().equals(servicio.name())) {
                return mecanico;
            }
        }
        return null;
    }

    private void vincularOrdenYMecanico(OrdenReparacion orden, Mecanico mecanico) {
        orden.asignarMecanico(mecanico);
        mecanico.asignarOrden(orden);
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
        if (tiposServicio.contiene(tipo)) {
            throw new IllegalArgumentException("El tipo de servicio ya está registrado: " + tipo);
        }
        tiposServicio.agregar(tipo);
    }

    // endregion

    // region Consultas y reportes (iteradores)

    public Iterator<OrdenReparacion> obtenerOrdenesPendientes() {
        return ordenesPendientes.iterator();
    }

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
        if (!mecanicos.contiene(mecanico)) {
            throw new IllegalArgumentException("El mecánico no pertenece a este taller");
        }
        return mecanico.getOrdenAsignada();
    }

    public Estadisticas generarEstadisticas() {

        int pendientes = 0, asignadas = 0, enReparacion = 0, reparadas = 0, entregadas = 0;
        Duration sumaTiempos = Duration.ZERO;
        int finalizadas = 0;

        for (OrdenReparacion orden : todasLasOrdenes) {
            switch (orden.getEstadoActual()) {
                case Recibida -> pendientes++;
                case Asignada -> asignadas++;
                case EnReparacion -> enReparacion++;
                case Reparada -> reparadas++;
                case Entregada -> entregadas++;
            }

            if (orden.getFechaFinalizacion() != null) {
                sumaTiempos = sumaTiempos.plus(orden.tiempoEnTaller());
                finalizadas++;
            }
        }

        Duration tiempoPromedio = (finalizadas == 0) ? Duration.ZERO : sumaTiempos.dividedBy(finalizadas);

        return new Estadisticas(todasLasOrdenes.tamano(), pendientes, asignadas,
                enReparacion, reparadas, entregadas, tiempoPromedio);
    }

    public int totalOrdenes() {
        return todasLasOrdenes.tamano();
    }

    public static class Estadisticas {
        public final int totalOrdenes;
        public final int ordenesRecibidas;
        public final int ordenesAsignadas;
        public final int ordenesEnReparacion;
        public final int ordenesReparadas;
        public final int ordenesEntregadas;
        public final Duration tiempoPromedioReparacion;

        public Estadisticas(int totalOrdenes, int ordenesRecibidas, int ordenesAsignadas,
                            int ordenesEnReparacion, int ordenesReparadas, int ordenesEntregadas,
                            Duration tiempoPromedioReparacion) {
            this.totalOrdenes = totalOrdenes;
            this.ordenesRecibidas = ordenesRecibidas;
            this.ordenesAsignadas = ordenesAsignadas;
            this.ordenesEnReparacion = ordenesEnReparacion;
            this.ordenesReparadas = ordenesReparadas;
            this.ordenesEntregadas = ordenesEntregadas;
            this.tiempoPromedioReparacion = tiempoPromedioReparacion;
        }

        @Override
        public String toString() {
            return "Estadisticas{total=" + totalOrdenes +
                    ", recibidas=" + ordenesRecibidas +
                    ", asignadas=" + ordenesAsignadas +
                    ", enReparacion=" + ordenesEnReparacion +
                    ", reparadas=" + ordenesReparadas +
                    ", entregadas=" + ordenesEntregadas +
                    ", tiempoPromedioReparacion=" + tiempoPromedioReparacion +
                    '}';
        }
    }

    // endregion
}