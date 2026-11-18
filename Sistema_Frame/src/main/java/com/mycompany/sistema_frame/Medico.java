package com.mycompany.sistema_frame;

import java.io.Serializable;

public class Medico implements Serializable {
    private int id;
    private String nombre;
    private Especialidad especialidad;

    public Medico(int id, String nombre, Especialidad especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }

    @Override
    public String toString() {
        return id + " - " + nombre + " (" + (especialidad != null ? especialidad.getNombre() : "Sin especialidad") + ")";
    }
}