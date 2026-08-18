package ADTs;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/*
 * Taller Práctico 01 - Sistema de Gestión de Taller de Reparación
 * Estructuras de Datos y Algoritmos - 2026-20
 *
 * María José Ledesma Cordoba - ID:000559241
 * Miguel Angel Puente Mejia - ID:000559418
 */

/**
 * ADT Bag (Bolsa genérica): colección sin orden garantizado y sin operación
 * de remover un elemento en particular. Se usa en TallerSystem para
 * "todasLasOrdenes", "mecanicos" y "tiposServicio": en esos casos solo
 * importa poder agregar, verificar pertenencia y recorrer todo; nunca hay
 * que "sacar" un elemento en particular como sí ocurre con la Queue.
 *
 * Se implementa sobre un arreglo dinámico interno (crece al duplicarse)
 * porque agregar al final es O(1) amortizado y no se necesita la
 * flexibilidad de nodos enlazados al no haber inserciones/borrados por un
 * extremo específico.
 *
 * @param <T> tipo de los elementos almacenados en la bolsa
 */
public class Bag<T> implements Iterable<T> {

    private static final int CAPACIDAD_INICIAL = 10;

    private Object[] elementos;
    private int tamano;

    public Bag() {
        this.elementos = new Object[CAPACIDAD_INICIAL];
        this.tamano = 0;
    }

    /** Agrega un elemento a la bolsa. O(1) amortizado. */
    public void agregar(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("No se puede agregar un elemento null");
        }

        if (tamano == elementos.length) {
            redimensionar(elementos.length * 2);
        }

        elementos[tamano] = elemento;
        tamano++;
    }

    /** Verifica si la bolsa contiene un elemento (según equals()). O(n). */
    public boolean contiene(T elemento) {
        for (int i = 0; i < tamano; i++) {
            if (elementos[i].equals(elemento)) {
                return true;
            }
        }
        return false;
    }

    public boolean estaVacia() {
        return tamano == 0;
    }

    public int tamano() {
        return tamano;
    }

    private void redimensionar(int nuevaCapacidad) {
        Object[] nuevoArreglo = new Object[nuevaCapacidad];
        System.arraycopy(elementos, 0, nuevoArreglo, 0, tamano);
        elementos = nuevoArreglo;
    }

    @Override
    public Iterator<T> iterator() {
        return new BagIterator();
    }

    /**
     * Retorna un iterador personalizado que recorre SOLO los elementos que
     * cumplen "filtro", sin copiar la bolsa a otra colección. Es la base de
     * obtenerOrdenesPorEstado(estado) y obtenerOrdenesPorTipo(tipo) en
     * TallerSystem, por ejemplo:
     *   bagDeOrdenes.iteradorFiltrado(o -> o.getEstadoActual() == Estado.Reparada)
     */
    public Iterator<T> iteradorFiltrado(Predicate<T> filtro) {
        if (filtro == null) {
            throw new IllegalArgumentException("El filtro no puede ser null");
        }
        return new BagFiltradoIterator(filtro);
    }

    @SuppressWarnings("unchecked")
    private class BagIterator implements Iterator<T> {

        private int indice = 0;

        @Override
        public boolean hasNext() {
            return indice < tamano;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No hay más elementos en la bolsa");
            }
            return (T) elementos[indice++];
        }
    }

    @SuppressWarnings("unchecked")
    private class BagFiltradoIterator implements Iterator<T> {

        private final Predicate<T> filtro;
        private int indice = 0;
        private T siguiente;
        private boolean haySiguiente;

        private BagFiltradoIterator(Predicate<T> filtro) {
            this.filtro = filtro;
            avanzar();
        }

        /** Adelanta el índice interno hasta el próximo elemento que cumpla el filtro (o hasta el final). */
        private void avanzar() {
            haySiguiente = false;
            while (indice < tamano) {
                T candidato = (T) elementos[indice++];
                if (filtro.test(candidato)) {
                    siguiente = candidato;
                    haySiguiente = true;
                    break;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return haySiguiente;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No hay más elementos que cumplan el filtro");
            }
            T resultado = siguiente;
            avanzar();
            return resultado;
        }
    }
}
