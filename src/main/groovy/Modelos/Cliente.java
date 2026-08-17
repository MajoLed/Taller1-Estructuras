package Modelos;

import java.util.UUID;

/*
 * Taller Práctico 01 - Sistema de Gestión de Taller de Reparación
 * Estructuras de Datos y Algoritmos - 2026-20
 *
 * María José Ledesma Cordoba - ID:000559241
 */

class Cliente {

    private int id; //C-001
    private String nombre; //nombre completo
    private String email;
    private String telefono;
    private String placaVehiculo;

    static int contador;

    public Cliente(String nombre, String telefono, String email, String placaVehiculo) {
        id = Cliente.contador++;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.placaVehiculo = placaVehiculo;
    }

    //Getters

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {

        //Control formato correcto
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Error: Correo inválido, escriba un formato válido por favor");
        }

        return email;

    }

    public String getTelefono() {
        return telefono;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    //Setters

    public void setNombre(String nombre) {
        if (nombre.trim().isEmpty())
            System.out.println("Error - el nombre no puede estar vacío");

        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }


}


