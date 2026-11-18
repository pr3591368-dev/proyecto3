package com.mycompany.sistema_frame;

import java.io.Serializable;

public class Especialidad implements Serializable {
    private int id;
    private String nombre;

    public Especialidad(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return nombre;
    }
}