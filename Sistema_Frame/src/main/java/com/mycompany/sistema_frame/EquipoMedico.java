package com.mycompany.sistema_frame;

import java.io.Serializable;

public class EquipoMedico implements Serializable {
    private int id;
    private String nombre;
    private int cantidad;

    public EquipoMedico(int id, String nombre, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}