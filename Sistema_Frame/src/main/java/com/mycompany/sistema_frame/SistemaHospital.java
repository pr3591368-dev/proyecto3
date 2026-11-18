package com.mycompany.sistema_frame;

import javax.swing.*;

public class SistemaHospital {
    public static void main(String[] args) {
        // Instanciamos la fachada que maneja la persistencia con archivos
        PersistenciaFachada fachada = new PersistenciaFachada();

        // Ejecutamos la Interfaz Gráfica en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            Sistema_Frame frame = new Sistema_Frame(fachada);
            frame.setVisible(true);
        });
    }
}