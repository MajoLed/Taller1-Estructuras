package ADTs;

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
 * ADT Queue (Cola FIFO genérica).
 *
 * Se implementa con una lista enlazada simple de nodos internos (no con un
 * arreglo) para poder encolar y desencolar en O(1) sin desplazar elementos
 * ni redimensionar. Se usa en TallerSystem como "ordenesPendientes", ya que
 * el requisito de negocio 1 exige que las órdenes se atiendan estrictamente
 * en orden de llegada.
 *
 * @param <T> tipo de los elementos almacenados en la cola
 */
public class Queue<T> implements Iterable<T> {

    private Nodo<T> frente;
    private Nodo<T> ultimo;
    private int tamano;

    private static class Nodo<T> {
        private final T dato;
        private Nodo<T> siguiente;

        private Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    public Queue() {
        this.frente = null;
        this.ultimo = null;
        this.tamano = 0;
    }

    /** Agrega un elemento al final de la cola. O(1). */
    public void encolar(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("No se puede encolar un elemento null");
        }

        Nodo<T> nuevo = new Nodo<>(elemento);

        if (esVacia()) {
            frente = nuevo;
        } else {
            ultimo.siguiente = nuevo;
        }

        ultimo = nuevo;
        tamano++;
    }

    /** Retira y retorna el elemento al frente de la cola. O(1). */
    public T desencolar() {
        if (esVacia()) {
            throw new NoSuchElementException("No se puede desencolar: la cola está vacía");
        }

        T dato = frente.dato;
        frente = frente.siguiente;
        tamano--;

        if (frente == null) {
            ultimo = null;
        }

        return dato;
    }

    /** Retorna el elemento al frente sin retirarlo. O(1). */
    public T verFrente() {
        if (esVacia()) {
            throw new NoSuchElementException("La cola está vacía");
        }
        return frente.dato;
    }

    public boolean esVacia() {
        return tamano == 0;
    }

    public int tamano() {
        return tamano;
    }

    @Override
    public Iterator<T> iterator() {
        return new QueueIterator();
    }

    /**
     * Iterador personalizado: recorre la cola de frente a final SIN
     * desencolar (solo lectura), respetando el orden FIFO real.
     */
    private class QueueIterator implements Iterator<T> {

        private Nodo<T> actual = frente;

        @Override
        public boolean hasNext() {
            return actual != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No hay más elementos en la cola");
            }
            T dato = actual.dato;
            actual = actual.siguiente;
            return dato;
        }
    }
}
