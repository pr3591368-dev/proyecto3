package com.mycompany.sistema_frame;

import java.util.List;

public interface IPersistenciaFachada {
    void agregarPaciente(Paciente paciente);
    Paciente obtenerPacientePorId(int id);
    List<Paciente> listarPacientes();
    List<Paciente> buscarPorNombre(String patron); // Requerimiento Proyecto 3

    void agregarMedico(Medico medico);
    Medico obtenerMedicoPorId(int id);
    List<Medico> listarMedicos();

    void agregarEspecialidad(Especialidad especialidad);
    Especialidad obtenerEspecialidadPorId(int id);
    List<Especialidad> listarEspecialidades();

    void agregarEquipoMedico(EquipoMedico equipo);
    void actualizarCantidadEquipo(int id, int cantidad);
    List<EquipoMedico> listarEquiposMedicos();

    void programarConsulta(Consulta consulta);
    List<Consulta> listarConsultas();
    List<Consulta> buscarConsultasPorFecha(String patronFecha); // Requerimiento Proyecto 3
}