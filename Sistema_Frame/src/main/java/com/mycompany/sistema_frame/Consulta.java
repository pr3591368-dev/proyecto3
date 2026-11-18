package com.mycompany.sistema_frame;

import java.io.Serializable;

public class Consulta implements Serializable {
    private int id;
    private Paciente paciente;
    private Medico medico;
    private String fecha; // Formato dd/MM/yyyy

    public Consulta(int id, Paciente paciente, Medico medico, String fecha) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public Paciente getpaciente() { return paciente; }
    public Medico getmedico() { return medico; }
    public String getfecha() { return fecha; }
}