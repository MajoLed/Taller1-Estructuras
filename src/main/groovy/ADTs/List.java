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
 * ADT List (Lista enlazada simple genérica).
 *
 * Se usa en TallerSystem como "historial": cada orden finalizada se agrega
 * SIEMPRE con agregarInicio(), de modo que recorrer la lista de cabeza a
 * cola entrega automáticamente las órdenes de la más reciente a la más
 * antigua (requisito de negocio 5), sin tener que ordenar por fecha en cada
 * consulta. Insertar al inicio es O(1); insertar al final también se ofrece
 * para completar el API, pero no se usa en el historial porque es O(n).
 *
 * @param <T> tipo de los elementos almacenados en la lista
 */
public class List<T> implements Iterable<T> {

    private Nodo<T> cabeza;
    private int tamano;

    private static class Nodo<T> {
        private final T dato;
        private Nodo<T> siguiente;

        private Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    public List() {
        this.cabeza = null;
        this.tamano = 0;
    }

    /** Inserta al inicio de la lista. O(1). Usado para el historial (más reciente primero). */
    public void agregarInicio(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("No se puede agregar un elemento null");
        }

        Nodo<T> nuevo = new Nodo<>(elemento);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
        tamano++;
    }

    /** Inserta al final de la lista. O(n). Se ofrece para completar el API. */
    public void agregarFinal(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("No se puede agregar un elemento null");
        }

        Nodo<T> nuevo = new Nodo<>(elemento);

        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }

        tamano++;
    }

    /** Retorna el elemento en la posición dada (0 = cabeza). O(n). */
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }

        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }
        return actual.dato;
    }

    public boolean estaVacia() {
        return tamano == 0;
    }

    public int tamano() {
        return tamano;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }

    /**
     * Iterador personalizado: recorre la lista de cabeza a cola. Como el
     * historial siempre inserta con agregarInicio(), este recorrido entrega
     * las órdenes finalizadas de la más reciente a la más antigua.
     */
    private class ListIterator implements Iterator<T> {

        private Nodo<T> actual = cabeza;

        @Override
        public boolean hasNext() {
            return actual != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No hay más elementos en la lista");
            }
            T dato = actual.dato;
            actual = actual.siguiente;
            return dato;
        }
    }
}
