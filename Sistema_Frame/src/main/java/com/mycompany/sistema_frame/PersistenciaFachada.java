package com.mycompany.sistema_frame;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PersistenciaFachada implements IPersistenciaFachada {
    private final Path dirPersistencia = Paths.get("datos_hospital");

    private final Path archivoPacientes = dirPersistencia.resolve("pacientes.dat");
    private final Path archivoMedicos = dirPersistencia.resolve("medicos.dat");
    private final Path archivoEspecialidades = dirPersistencia.resolve("especialidades.dat");
    private final Path archivoInventario = dirPersistencia.resolve("inventario.dat");
    private final Path archivoConsultas = dirPersistencia.resolve("consultas.dat");

    public PersistenciaFachada() {
        try {
            if (!Files.exists(dirPersistencia)) {
                Files.createDirectories(dirPersistencia);
            }
        } catch (IOException e) {
            System.err.println("Error al crear el directorio de persistencia: " + e.getMessage());
        }
    }

    @Override
    public void agregarPaciente(Paciente p) {
        List<Paciente> lista = listarPacientes();
        lista.add(p);
        guardar(archivoPacientes, lista);
    }

    @Override
    public Paciente obtenerPacientePorId(int id) {
        return listarPacientes().stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Paciente> listarPacientes() {
        return cargar(archivoPacientes);
    }

    @Override
    public List<Paciente> buscarPorNombre(String patron) {
        Pattern pattern = Pattern.compile("(?i).*" + Pattern.quote(patron) + ".*");
        return listarPacientes().stream()
                .filter(p -> pattern.matcher(p.getNombre()).matches())
                .collect(Collectors.toList());
    }

    @Override
    public void agregarMedico(Medico m) {
        List<Medico> lista = listarMedicos();
        lista.add(m);
        guardar(archivoMedicos, lista);
    }

    @Override
    public Medico obtenerMedicoPorId(int id) {
        return listarMedicos().stream().filter(m -> m.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Medico> listarMedicos() {
        return cargar(archivoMedicos);
    }

    @Override
    public void agregarEspecialidad(Especialidad e) {
        List<Especialidad> lista = listarEspecialidades();
        lista.add(e);
        guardar(archivoEspecialidades, lista);
    }

    @Override
    public Especialidad obtenerEspecialidadPorId(int id) {
        return listarEspecialidades().stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Especialidad> listarEspecialidades() {
        return cargar(archivoEspecialidades);
    }

    @Override
    public void agregarEquipoMedico(EquipoMedico eq) {
        List<EquipoMedico> lista = listarEquiposMedicos();
        lista.add(eq);
        guardar(archivoInventario, lista);
    }

    @Override
    public void actualizarCantidadEquipo(int id, int cantidad) {
        List<EquipoMedico> lista = listarEquiposMedicos();
        for (EquipoMedico eq : lista) {
            if (eq.getId() == id) {
                eq.setCantidad(cantidad);
                break;
            }
        }
        guardar(archivoInventario, lista);
    }

    @Override
    public List<EquipoMedico> listarEquiposMedicos() {
        return cargar(archivoInventario);
    }

    @Override
    public void programarConsulta(Consulta c) {
        List<Consulta> lista = listarConsultas();
        lista.add(c);
        guardar(archivoConsultas, lista);
    }

    @Override
    public List<Consulta> listarConsultas() {
        return cargar(archivoConsultas);
    }

    @Override
    public List<Consulta> buscarConsultasPorFecha(String patronFecha) {
        Pattern pattern = Pattern.compile(".*" + Pattern.quote(patronFecha) + ".*");
        return listarConsultas().stream()
                .filter(c -> pattern.matcher(c.getfecha()).matches())
                .collect(Collectors.toList());
    }

    private <T> void guardar(Path path, List<T> lista) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo " + path.getFileName() + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> cargar(Path path) {
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al leer el archivo " + path.getFileName() + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}