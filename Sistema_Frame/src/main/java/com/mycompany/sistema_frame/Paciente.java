package com.mycompany.sistema_frame;

import java.io.Serializable;

public class Paciente implements Serializable {
    private int id;
    private String nombre;
    private int edad;
    private String direccion;

    public Paciente(int id, String nombre, int edad, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.direccion = direccion;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}