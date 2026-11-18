package com.mycompany.sistema_frame;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Sistema_Frame extends JFrame {
    private final IPersistenciaFachada fachada;
    private final CardLayout cartas = new CardLayout();
    private final JPanel contenido = new JPanel(cartas);
    private final Map<String, Runnable> refrescos = new LinkedHashMap<>();

    public Sistema_Frame(IPersistenciaFachada fachada) {
        this.fachada = fachada;

        setTitle("Sistema Archivos Hospitalarios - Proyecto 3");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        crearSeccionPacientes();
        crearSeccionMedicos();
        crearSeccionEspecialidades();
        crearSeccionInventario();
        crearSeccionConsultas();
        crearSeccionBusquedaPacientes(); // Nueva sección Proyecto 3
        crearSeccionBusquedaConsultas(); // Nueva sección Proyecto 3

        add(crearCabecera(), BorderLayout.NORTH);
        add(contenido, BorderLayout.CENTER);

        mostrar("Pacientes");
    }

    private static final Color CAFE = new Color(0x5E3A33);

    private JPanel crearCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(CAFE);
        cabecera.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel titulo = new JLabel("Sistema Hospitalario");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        nav.setOpaque(false);
        
        ButtonGroup grupo = new ButtonGroup();
        for (String nombre : refrescos.keySet()) {
            BotonNav boton = new BotonNav(nombre);
            boton.addActionListener(e -> mostrar(nombre));
            grupo.add(boton);
            nav.add(boton);
            if (nombre.equals("Pacientes")) {
                boton.setSelected(true);
            }
        }

        cabecera.add(titulo, BorderLayout.WEST);
        cabecera.add(nav, BorderLayout.EAST);
        return cabecera;
    }

    private void mostrar(String nombre) {
        Runnable r = refrescos.get(nombre);
        if (r != null) r.run();      
        cartas.show(contenido, nombre);
    }

    private static class BotonNav extends JToggleButton {
        private boolean hover = false;

        BotonNav(String texto) {
            super(texto);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addItemListener(e -> setForeground(isSelected() ? CAFE : Color.WHITE));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isSelected()) {
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            } else if (hover) {
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private void crearSeccionPacientes() {
        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtEdad = new JTextField();
        JTextField txtDir = new JTextField();
        JButton btnGuardar = new JButton("Guardar Paciente");

        DefaultTableModel modelo = crearModelo("ID", "Nombre", "Edad", "Dirección");

        Runnable refrescar = () -> {
            modelo.setRowCount(0);
            for (Paciente p : fachada.listarPacientes()) {
                modelo.addRow(new Object[]{p.getId(), p.getNombre(), p.getEdad(), p.getDireccion()});
            }
        };

        btnGuardar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText().trim());
                String nombre = txtNombre.getText().trim();
                int edad = Integer.parseInt(txtEdad.getText().trim());
                String dir = txtDir.getText().trim();

                // Validaciones con Regex del Proyecto 3
                if (!Validador.validaNombrePaciente(nombre)) {
                    mensaje("Nombre inválido. Solo letras y máximo 50 caracteres.");
                    return;
                }
                if (!Validador.validaEdad(edad)) {
                    mensaje("Edad inválida. Debe estar entre 0 y 120 años.");
                    return;
                }
                if (!Validador.validaDireccion(dir)) {
                    mensaje("Dirección inválida. Contiene caracteres no permitidos.");
                    return;
                }
                if (fachada.obtenerPacientePorId(id) != null) {
                    mensaje("Ya existe un paciente con ese ID.");
                    return;
                }

                fachada.agregarPaciente(new Paciente(id, nombre, edad, dir));
                limpiar(txtId, txtNombre, txtEdad, txtDir);
                refrescar.run();
                mensaje("Paciente guardado con éxito.");
            } catch (NumberFormatException ex) {
                mensaje("ID y Edad deben ser números enteros válidos.");
            }
        });

        JPanel form = crearFormulario("Nuevo paciente (Validado)",
                new String[]{"ID:", "Nombre:", "Edad:", "Dirección:"},
                new JComponent[]{txtId, txtNombre, txtEdad, txtDir},
                btnGuardar);

        registrar("Pacientes", form, modelo, refrescar);
    }

    private void crearSeccionMedicos() {
        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JComboBox<Especialidad> cbEspecialidad = new JComboBox<>();
        JButton btnGuardar = new JButton("Guardar Médico");

        DefaultTableModel modelo = crearModelo("ID", "Nombre", "Especialidad");

        Runnable refrescar = () -> {
            Especialidad seleccionada = (Especialidad) cbEspecialidad.getSelectedItem();
            cbEspecialidad.removeAllItems();
            for (Especialidad es : fachada.listarEspecialidades()) {
                cbEspecialidad.addItem(es);
            }
            if (seleccionada != null) cbEspecialidad.setSelectedItem(seleccionada);

            modelo.setRowCount(0);
            for (Medico m : fachada.listarMedicos()) {
                modelo.addRow(new Object[]{m.getId(), m.getNombre(), m.getEspecialidad().getNombre()});
            }
        };

        btnGuardar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText().trim());
                String nombre = txtNombre.getText().trim();
                Especialidad esp = (Especialidad) cbEspecialidad.getSelectedItem();

                if (!Validador.validaNombreMedico(nombre)) {
                    mensaje("Nombre de médico inválido. Solo letras y máx 50 caracteres.");
                    return;
                }
                if (esp == null) {
                    mensaje("Primero registra al menos una especialidad.");
                    return;
                }
                if (fachada.obtenerMedicoPorId(id) != null) {
                    mensaje("Ya existe un médico con ese ID.");
                    return;
                }

                fachada.agregarMedico(new Medico(id, nombre, esp));
                limpiar(txtId, txtNombre);
                refrescar.run();
                mensaje("Médico guardado con éxito.");
            } catch (NumberFormatException ex) {
                mensaje("El ID debe ser un número entero.");
            }
        });

        JPanel form = crearFormulario("Nuevo médico (Validado)",
                new String[]{"ID:", "Nombre:", "Especialidad:"},
                new JComponent[]{txtId, txtNombre, cbEspecialidad},
                btnGuardar);

        registrar("Médicos", form, modelo, refrescar);
    }

    private void crearSeccionEspecialidades() {
        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JButton btnGuardar = new JButton("Guardar Especialidad");

        DefaultTableModel modelo = crearModelo("ID", "Nombre");

        Runnable refrescar = () -> {
            modelo.setRowCount(0);
            for (Especialidad es : fachada.listarEspecialidades()) {
                modelo.addRow(new Object[]{es.getId(), es.getNombre()});
            }
        };

        btnGuardar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText().trim());
                String nombre = txtNombre.getText().trim();

                if (!Validador.validaEspecialidad(nombre)) {
                    mensaje("Especialidad inválida. Solo letras y espacios, máx 30 caracteres.");
                    return;
                }
                if (fachada.obtenerEspecialidadPorId(id) != null) {
                    mensaje("Ya existe una especialidad con ese ID.");
                    return;
                }

                fachada.agregarEspecialidad(new Especialidad(id, nombre));
                limpiar(txtId, txtNombre);
                refrescar.run();
                mensaje("Especialidad guardada con éxito.");
            } catch (NumberFormatException ex) {
                mensaje("El ID debe ser un número entero.");
            }
        });

        JPanel form = crearFormulario("Nueva especialidad (Validada)",
                new String[]{"ID:", "Nombre:"},
                new JComponent[]{txtId, txtNombre},
                btnGuardar);

        registrar("Especialidades", form, modelo, refrescar);
    }

    private void crearSeccionInventario() {
        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtCantidad = new JTextField();
        JButton btnGuardar = new JButton("Guardar Equipo");
        JButton btnActualizar = new JButton("Actualizar Stock");

        DefaultTableModel modelo = crearModelo("ID", "Equipo", "Cantidad");

        Runnable refrescar = () -> {
            modelo.setRowCount(0);
            for (EquipoMedico eq : fachada.listarEquiposMedicos()) {
                modelo.addRow(new Object[]{eq.getId(), eq.getNombre(), eq.getCantidad()});
            }
        };

        btnGuardar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText().trim());
                String nombre = txtNombre.getText().trim();
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());

                if (!Validador.validaNombreEquipo(nombre)) {
                    mensaje("Nombre de equipo inválido. Solo letras y números, máx 30 caracteres.");
                    return;
                }
                if (!Validador.validaCantidadEquipo(cantidad)) {
                    mensaje("La cantidad debe ser un entero positivo mayor a 0.");
                    return;
                }
                if (existeEquipo(id)) {
                    mensaje("Ya existe un equipo con ese ID.");
                    return;
                }

                fachada.agregarEquipoMedico(new EquipoMedico(id, nombre, cantidad));
                limpiar(txtId, txtNombre, txtCantidad);
                refrescar.run();
                mensaje("Equipo guardado con éxito.");
            } catch (NumberFormatException ex) {
                mensaje("ID y Cantidad deben ser números enteros.");
            }
        });

        btnActualizar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText().trim());
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());

                if (!Validador.validaCantidadEquipo(cantidad)) {
                    mensaje("La cantidad debe ser un entero positivo.");
                    return;
                }
                if (!existeEquipo(id)) {
                    mensaje("No existe un equipo con ese ID.");
                    return;
                }

                fachada.actualizarCantidadEquipo(id, cantidad);
                refrescar.run();
                mensaje("Cantidad actualizada con éxito.");
            } catch (NumberFormatException ex) {
                mensaje("ID y Cantidad deben ser números enteros.");
            }
        });

        JPanel form = crearFormulario("Inventario de Equipos (Validado)",
                new String[]{"ID:", "Nombre Equipo:", "Cantidad:"},
                new JComponent[]{txtId, txtNombre, txtCantidad},
                btnActualizar, btnGuardar);

        registrar("Inventario", form, modelo, refrescar);
    }

    private boolean existeEquipo(int id) {
        return fachada.listarEquiposMedicos().stream().anyMatch(eq -> eq.getId() == id);
    }

    private void crearSeccionConsultas() {
        JTextField txtId = new JTextField();
        JComboBox<Paciente> cbPaciente = new JComboBox<>();
        JComboBox<Medico> cbMedico = new JComboBox<>();
        JTextField txtFecha = new JTextField();
        JButton btnProgramar = new JButton("Programar Consulta");

        DefaultTableModel modelo = crearModelo("ID", "Paciente", "Médico", "Fecha");

        Runnable refrescar = () -> {
            Paciente pSel = (Paciente) cbPaciente.getSelectedItem();
            Medico mSel = (Medico) cbMedico.getSelectedItem();

            cbPaciente.removeAllItems();
            for (Paciente p : fachada.listarPacientes()) cbPaciente.addItem(p);
            if (pSel != null) cbPaciente.setSelectedItem(pSel);

            cbMedico.removeAllItems();
            for (Medico m : fachada.listarMedicos()) cbMedico.addItem(m);
            if (mSel != null) cbMedico.setSelectedItem(mSel);

            modelo.setRowCount(0);
            for (Consulta c : fachada.listarConsultas()) {
                modelo.addRow(new Object[]{
                    c.getId(), c.getpaciente().getNombre(), c.getmedico().getNombre(), c.getfecha()
                });
            }
        };

        btnProgramar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText().trim());
                Paciente p = (Paciente) cbPaciente.getSelectedItem();
                Medico m = (Medico) cbMedico.getSelectedItem();
                String fecha = txtFecha.getText().trim();

                if (!Validador.validaIdConsulta(id)) {
                    mensaje("El ID de consulta debe ser positivo.");
                    return;
                }
                if (p == null || m == null) {
                    mensaje("Debe registrar al menos un paciente y un médico.");
                    return;
                }
                // Validación exacta de formato dd/MM/yyyy (Proyecto 3)
                if (!Validador.validaFechaConsulta(fecha)) {
                    mensaje("Fecha inválida. Debe cumplir estrictamente con el formato dd/MM/yyyy (ej. 07/01/2025).");
                    return;
                }
                if (fachada.listarConsultas().stream().anyMatch(c -> c.getId() == id)) {
                    mensaje("Ya existe una consulta con ese ID.");
                    return;
                }

                fachada.programarConsulta(new Consulta(id, p, m, fecha));
                limpiar(txtId, txtFecha);
                refrescar.run();
                mensaje("Consulta programada con éxito.");
            } catch (NumberFormatException ex) {
                mensaje("El ID debe ser un número entero.");
            }
        });

        JPanel form = crearFormulario("Programar consulta (Fecha dd/MM/yyyy)",
                new String[]{"ID Consulta:", "Paciente:", "Médico:", "Fecha (dd/MM/yyyy):"},
                new JComponent[]{txtId, cbPaciente, cbMedico, txtFecha},
                btnProgramar);

        registrar("Consultas", form, modelo, refrescar);
    }

    // --- NUEVAS SECCIONES REQUERIDAS EN EL PROYECTO 3 (Búsquedas por Patrón) ---

    private void crearSeccionBusquedaPacientes() {
        JTextField txtPatron = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar Pacientes");
        DefaultTableModel modelo = crearModelo("ID", "Nombre", "Edad", "Dirección");
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(24);

        btnBuscar.addActionListener(e -> {
            String patron = txtPatron.getText().trim();
            modelo.setRowCount(0);
            for (Paciente p : fachada.buscarPorNombre(patron)) {
                modelo.addRow(new Object[]{p.getId(), p.getNombre(), p.getEdad(), p.getDireccion()});
            }
        });

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Patrón de Nombre:"));
        panelSuperior.add(txtPatron);
        panelSuperior.add(btnBuscar);
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Búsqueda de Pacientes por Patrón"));

        JPanel seccion = new JPanel(new BorderLayout(10, 10));
        seccion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        seccion.add(panelSuperior, BorderLayout.NORTH);
        seccion.add(new JScrollPane(tabla), BorderLayout.CENTER);

        contenido.add(seccion, "Buscar Pacientes");
        refrescos.put("Buscar Pacientes", () -> {
            modelo.setRowCount(0);
            for (Paciente p : fachada.listarPacientes()) {
                modelo.addRow(new Object[]{p.getId(), p.getNombre(), p.getEdad(), p.getDireccion()});
            }
        });
    }

    private void crearSeccionBusquedaConsultas() {
        JTextField txtPatronFecha = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar Consultas");
        DefaultTableModel modelo = crearModelo("ID", "Paciente", "Médico", "Fecha");
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(24);

        btnBuscar.addActionListener(e -> {
            String patron = txtPatronFecha.getText().trim();
            modelo.setRowCount(0);
            for (Consulta c : fachada.buscarConsultasPorFecha(patron)) {
                modelo.addRow(new Object[]{c.getId(), c.getpaciente().getNombre(), c.getmedico().getNombre(), c.getfecha()});
            }
        });

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Patrón de Fecha (dd/MM/yyyy):"));
        panelSuperior.add(txtPatronFecha);
        panelSuperior.add(btnBuscar);
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Búsqueda de Consultas por Fecha"));

        JPanel seccion = new JPanel(new BorderLayout(10, 10));
        seccion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        seccion.add(panelSuperior, BorderLayout.NORTH);
        seccion.add(new JScrollPane(tabla), BorderLayout.CENTER);

        contenido.add(seccion, "Buscar Consultas");
        refrescos.put("Buscar Consultas", () -> {
            modelo.setRowCount(0);
            for (Consulta c : fachada.listarConsultas()) {
                modelo.addRow(new Object[]{c.getId(), c.getpaciente().getNombre(), c.getmedico().getNombre(), c.getfecha()});
            }
        });
    }

    // --- Métodos de Utilidad Gráficos ---

    private void registrar(String nombre, JPanel form, DefaultTableModel modelo, Runnable refrescar) {
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(24);

        JPanel seccion = new JPanel(new BorderLayout(10, 10));
        seccion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        seccion.add(form, BorderLayout.NORTH);
        seccion.add(new JScrollPane(tabla), BorderLayout.CENTER);

        contenido.add(seccion, nombre);
        refrescos.put(nombre, refrescar);
        refrescar.run();
    }

    private JPanel crearFormulario(String titulo, String[] etiquetas, JComponent[] campos, JButton... botones) {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(titulo));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);

        for (int i = 0; i < etiquetas.length; i++) {
            g.gridy = i;
            g.gridx = 0;
            g.weightx = 0;
            g.fill = GridBagConstraints.NONE;
            g.anchor = GridBagConstraints.WEST;
            form.add(new JLabel(etiquetas[i]), g);

            g.gridx = 1;
            g.weightx = 1;
            g.fill = GridBagConstraints.HORIZONTAL;
            form.add(campos[i], g);
        }

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        for (JButton b : botones) panelBotones.add(b);

        g.gridy = etiquetas.length;
        g.gridx = 0;
        g.gridwidth = 2;
        g.insets = new Insets(8, 6, 6, 6);
        form.add(panelBotones, g);

        return form;
    }

    private DefaultTableModel crearModelo(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    private void limpiar(JTextField... campos) {
        for (JTextField c : campos) c.setText("");
    }

    private void mensaje(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }
}