package com.mycompany.sistema_frame;

import java.util.regex.Pattern;

public class Validador {
    // a. Nombre Paciente: Solo letras, máx 50 caracteres
    private static final Pattern PATRON_NOMBRE_PACIENTE = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{1,50}$");
    
    // c. Dirección: Letras, dígitos, espacios y símbolos comunes (comas, puntos, etc.)
    private static final Pattern PATRON_DIRECCION = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s,#\\.\\-_]+$");
    
    // a. Nombre Médico: Solo letras, máx 50 caracteres (permite puntos y espacios por "Dr.")
    private static final Pattern PATRON_NOMBRE_MEDICO = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\.\\s]{1,50}$");
    
    // b. Especialidad: Solo letras y espacios, máx 30 caracteres
    private static final Pattern PATRON_ESPECIALIDAD = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{1,30}$");
    
    // a. Nombre Equipo: Solo letras y números, máx 30 caracteres
    private static final Pattern PATRON_NOMBRE_EQUIPO = Pattern.compile("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]{1,30}$");
    
    // a. Fecha Consulta: Formato estricto dd/MM/yyyy
    private static final Pattern PATRON_FECHA = Pattern.compile("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$");

    public static boolean validaNombrePaciente(String nombre) {
        return nombre != null && PATRON_NOMBRE_PACIENTE.matcher(nombre).matches();
    }

    public static boolean validaEdad(int edad) {
        return edad >= 0 && edad <= 120;
    }

    public static boolean validaDireccion(String direccion) {
        return direccion != null && PATRON_DIRECCION.matcher(direccion).matches();
    }

    public static boolean validaNombreMedico(String nombre) {
        return nombre != null && PATRON_NOMBRE_MEDICO.matcher(nombre).matches();
    }

    public static boolean validaEspecialidad(String especialidad) {
        return especialidad != null && PATRON_ESPECIALIDAD.matcher(especialidad).matches();
    }

    public static boolean validaNombreEquipo(String nombre) {
        return nombre != null && PATRON_NOMBRE_EQUIPO.matcher(nombre).matches();
    }

    public static boolean validaCantidadEquipo(int cantidad) {
        return cantidad > 0;
    }

    public static boolean validaFechaConsulta(String fecha) {
        return fecha != null && PATRON_FECHA.matcher(fecha).matches();
    }

    public static boolean validaIdConsulta(int id) {
        return id > 0;
    }
}