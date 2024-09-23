/**
 * Integrantes:
 * CERVANTES CRUZ EDGAR
 * GARCIA TRINIDAD EMIR ESTEBAN
 * LOPEZ DEL CASTILLO DANIEL ISAY
 * PEREZ MENDIETA ALDO
*/

package visual;

import extra.PanelEstacion;
import clases.Estacion;
import creargrafo.CrearGrafo;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import clases.Metro;
import extra.InfoRecorrido;
import extra.Colors;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Clase que dibuja una ventana, en donde se mostrará el metro, y los botones
 * necwsarios para interactuar con éste.
 */
public class Window extends JFrame {
    
    /**
     * Estación que es seleccionada actualmente.
     */
    private Estacion selected;
    
    /**
     * Crea una ventana.
     */
    public Window() {
        initComponents();
    }
    
    /**
     * Método llamado cada vez que el valor de la Estación seleccionada cambia,
     * en caso de no haber Estación seleccionada, se desabilitarán algunos botones.
     * 
     * @param e Valor de la estación seleccionada.
     */
    public void updateSelected(Estacion e) {
        selected = e;
        if(e == null) {
            botonAgregar.setEnabled(false);
            botonPrim.setEnabled(false);
            botonDFS.setEnabled(false);
            botonBFS.setEnabled(false);
            return;
        }
        boolean value = ! (listaEstaciones.getComponentCount() > 0 && ((PanelEstacion) listaEstaciones.getComponent(listaEstaciones.getComponentCount() - 1))
                .getEstacion().equals(e));
        botonAgregar.setEnabled(value);
        botonPrim.setEnabled(true);
        botonDFS.setEnabled(true);
        botonBFS.setEnabled(true);
    }
    
    /**
     * Da formato a la ventana, agrega los botones, y eventos necesarios.
     */
    private void initComponents() {
        setTitle("Metro CDMX");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                windowKeyPressed(evt);
            }
        });
        
        metro = new Metro(CrearGrafo.crearGrafo());
        metro.setParent(this);
        panelCentro = new JScrollPane(metro);
        
        panelBotones = new JPanel();
        panelBotones.setBackground(Color.white);
        
        botonAyuda = new JButton("?");
        botonAyuda.setBackground(Colors.EXTRA_COLOR);
        botonAyuda.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonAyuda.setToolTipText("Ayuda (H)");
        botonAyuda.setFont(new Font("Century Gothic", 1, 14));
        botonAyuda.setFocusable(false);
        botonAyuda.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                botonAyudaMousePressed(evt);
            }
        });
        
        botonReiniciar = new JButton("Reiniciar");
        botonReiniciar.setBackground(new Color(212, 108, 148));
        botonReiniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonReiniciar.setToolTipText("Muestra el mapa original (R)");
        botonReiniciar.setFont(new Font("Century Gothic", 1, 14));
        botonReiniciar.setFocusable(false);
        botonReiniciar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                botonReiniciarMousePressed(evt);
            }
        });
        
        botonAgregar = new JButton("Agregar");
        botonAgregar.setBackground(Colors.DIJKSTRA_COLOR);
        botonAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonAgregar.setToolTipText("Agregar un destino/parada (A)");
        botonAgregar.setFont(new Font("Century Gothic", 1, 14));
        botonAgregar.setFocusable(false);
        botonAgregar.setEnabled(false);
        botonAgregar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                botonAgregarMousePressed(evt);
            }
        });
        
        botonEliminar = new JButton("Eliminar");
        botonEliminar.setBackground(Colors.DIJKSTRA_COLOR);
        botonEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonEliminar.setToolTipText("Elimina el último destino (Supr)");
        botonEliminar.setFont(new Font("Century Gothic", 1, 14));
        botonEliminar.setFocusable(false);
        botonEliminar.setEnabled(false);
        botonEliminar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                eliminarEstacion();
            }
        });
        
        botonIniciar = new JButton("Iniciar");
        botonIniciar.setBackground(Colors.DIJKSTRA_COLOR);
        botonIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonIniciar.setToolTipText("Mostrar el camino más corto (S)");
        botonIniciar.setFont(new Font("Century Gothic", 1, 14));
        botonIniciar.setFocusable(false);
        botonIniciar.setEnabled(false);
        botonIniciar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                botonIniciarMousePressed(evt);
            }
        });
        
        botonPasoAPaso = new JButton("Paso a paso");
        botonPasoAPaso.setBackground(Colors.DIJKSTRA_COLOR);
        botonPasoAPaso.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonPasoAPaso.setToolTipText("Mostrar el recorrido paso a paso (P)");
        botonPasoAPaso.setFont(new Font("Century Gothic", 1, 14));
        botonPasoAPaso.setFocusable(false);
        botonPasoAPaso.setEnabled(false);
        botonPasoAPaso.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                botonPasoAPasoMousePressed(evt);
            }
        });
        
        botonAnterior = new JButton("<<");
        botonAnterior.setBackground(Colors.DIJKSTRA_COLOR);
        botonAnterior.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonAnterior.setToolTipText("Estación anterior (Izquierda)");
        botonAnterior.setFont(new Font("Century Gothic", 1, 14));
        botonAnterior.setFocusable(false);
        botonAnterior.setEnabled(false);
        botonAnterior.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                botonSiguienteMousePressed(evt, -1);
            }
        });

        botonSiguiente = new JButton(">>");
        botonSiguiente.setBackground(Colors.DIJKSTRA_COLOR);
        botonSiguiente.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonSiguiente.setToolTipText("Estación siguiente (Derecha)");
        botonSiguiente.setFont(new Font("Century Gothic", 1, 14));
        botonSiguiente.setFocusable(false);
        botonSiguiente.setEnabled(false);
        botonSiguiente.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                botonSiguienteMousePressed(evt, 1);
            }
        });
        
        mostrarNombres = new JCheckBox("Nombres", true);
        mostrarNombres.setFocusable(false);
        mostrarNombres.setBackground(Color.white);
        mostrarNombres.setToolTipText("Mostrar/ocultar nombres (M)");
        mostrarNombres.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                mostrarNombresItemStateChanged(e);
            }
            
        });
        
        cambiarZoom = new JSlider(50, 200, 87);
        metro.setZoom(getZoom());
        cambiarZoom.setFocusable(false);
        cambiarZoom.setBackground(Color.white);
        cambiarZoom.setToolTipText("Cambiar zoom (+ Arriba / Abajo -)");
        cambiarZoom.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                cambiarZoomStateChanged(e);
            }
            
        });
        
        zoomLabel = new JLabel((int) (getZoom() * 100) + "%");
        zoomLabel.setFont(new Font("Century Gothic", 1, 14));

        botonPrim = new JButton("AEM");
        botonPrim.setBackground(Colors.EXTRA_COLOR);
        botonPrim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonPrim.setToolTipText("Árbol de Expansión Mínima (ENTER)");
        botonPrim.setFont(new Font("Century Gothic", 1, 14));
        botonPrim.setFocusable(false);
        botonPrim.setEnabled(false);
        botonPrim.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                botonPrimMousePressed(evt);
            }
        });

        botonDFS = new JButton("DFS");
        botonDFS.setBackground(Colors.EXTRA_COLOR);
        botonDFS.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonDFS.setToolTipText("Búsqueda en Profundidad (D)");
        botonDFS.setFont(new Font("Century Gothic", 1, 14));
        botonDFS.setFocusable(false);
        botonDFS.setEnabled(false);
        botonDFS.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                botonDFSMousePressed(evt);
            }
        });
        
        botonBFS = new JButton("BFS");
        botonBFS.setBackground(Colors.EXTRA_COLOR);
        botonBFS.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonBFS.setToolTipText("Búsqueda en anchura (B)");
        botonBFS.setFont(new Font("Century Gothic", 1, 14));
        botonBFS.setFocusable(false);
        botonBFS.setEnabled(false);
        botonBFS.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                botonBFSMousePressed(evt);
            }
        });
        
        panelBotones.add(botonAyuda);
        panelBotones.add(botonReiniciar);
        panelBotones.add(botonAgregar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonIniciar);
        panelBotones.add(botonPasoAPaso);
        panelBotones.add(botonAnterior);
        panelBotones.add(botonSiguiente);
        panelBotones.add(mostrarNombres);
        panelBotones.add(cambiarZoom);
        panelBotones.add(zoomLabel);
        panelBotones.add(botonPrim);
        panelBotones.add(botonDFS);
        panelBotones.add(botonBFS);
        
        panelAbajo = new JScrollPane(panelBotones);
        panelAbajo.setPreferredSize(new Dimension(0, 58));
        
        listaEstaciones = new JPanel(new GridLayout(0, 1, 0, 5));
        listaEstaciones.setBackground(Color.white);
        
        panelEstaciones = new JScrollPane(listaEstaciones);
        panelEstaciones.setBorder(null);
        panelEstaciones.setPreferredSize(new Dimension(220, 0));
        panelEstaciones.setHorizontalScrollBar(null);
        
        title = new JLabel("¿Cómo llegar?");
        title.setFont(new Font("Century Gothic", 1, 14));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setPreferredSize(new Dimension(220, 50));
        
        panelInfo = new InfoRecorrido();
        
        panelIzquierdo = new JPanel(new BorderLayout(2, 2));
        panelIzquierdo.setBackground(Color.white);
        
        panelIzquierdo.add(panelEstaciones, BorderLayout.CENTER);
        panelIzquierdo.add(title, BorderLayout.NORTH);
        panelIzquierdo.add(panelInfo, BorderLayout.SOUTH);
        
        add(panelCentro, BorderLayout.CENTER);
        add(panelAbajo, BorderLayout.PAGE_END);
        add(panelIzquierdo, BorderLayout.LINE_START);
        setVisible(true);
    }
    
    /**
     * Método que muestra un panel con información acerca del funcionamiento del
     * programa.
     */
    private void ayuda() {
        String txt = """
                     Haz clic sobre una estación para seleccionarla,
                     después podrás iniciar alguno de los recorridos,
                     "AEM", "DFS, "BFS", o agregarla a la lista de
                     destinos/paradas.
                     Luego de tener al menos dos destinos en esta
                     lista, podrás iniciar el recorrido dando clic
                     en iniciar; en donde luego podrás visualizar
                     el paso a paso del recorrido.
                     """;
        JOptionPane.showMessageDialog(null, txt, "Ayuda", -1);
    }
    
    /**
     * Método elimina los recorridos, y muestra todas las estaciones.
     */
    private void reiniciar() {
        metro.mostrarEstaciones();
        metro.reestartRecorrido();
        botonPasoAPaso.setEnabled(false);
        botonAnterior.setEnabled(false);
        botonSiguiente.setEnabled(false);
        botonIniciar.setEnabled(listaEstaciones.getComponentCount() >= 2);
        panelInfo.endRecorrido();
    }
    
    /**
     * Método para agregar una estación a los destinos / paradas, solo puede ser
     * ejecutado si hay alguna estación seleccionada, y además ésta no coincide
     * con la útlima agregada a ésta lista.
     */
    private void agregarEstacion() {
        if(! botonAgregar.isEnabled()) {
            return;
        }
        listaEstaciones.add(new PanelEstacion(metro.getSelected()));
        botonEliminar.setEnabled(true);
        reiniciar();
        if(listaEstaciones.getComponentCount() >= 2) {
            botonIniciar.setEnabled(true);
        }
        listaEstaciones.updateUI();
    }
    
    /**
     * Elimina la última estación de la lista de destinos / paradas, solo puede
     * ser llamado si hay al menos una Estación en ésta lista.
     */
    private void eliminarEstacion() {
        if(! botonEliminar.isEnabled()) {
            return;
        }
        listaEstaciones.remove(listaEstaciones.getComponentCount() - 1);
        reiniciar();
        botonEliminar.setEnabled(listaEstaciones.getComponentCount() != 0);
        listaEstaciones.updateUI();
    }
    
    /**
     * Método usado para iniciar el recorrido de camino más corto, solo puede ser
     * llamado si hay al menos dos Estaciones en la lista de destinos / paradas.
     * Actualiza los valores de la información de recorrido, que contiene la distancia,
     * el número de Estaciones, y el número de transbordes.
     */
    private void iniciarRecorrido() {
        if(! botonIniciar.isEnabled()) {
            return;
        }
        reiniciar();
        Component[] ff = listaEstaciones.getComponents();
        Estacion a = ((PanelEstacion) ff[0]).getEstacion();
        for(int i = 1 ; i < ff.length ; i ++) {
            if(! metro.addDijkstra(a, ((PanelEstacion) ff[i]).getEstacion())) {
                System.out.println("IMPOSIBLE????");
            }
            metro.addDestino(((PanelEstacion) ff[i]).getEstacion());
            a = ((PanelEstacion) ff[i]).getEstacion();
        }
        metro.mostrarRecorrido();
        panelInfo.startRecorrido(metro.distancia(), metro.estaciones(), metro.transbordes());
        botonPasoAPaso.setEnabled(true);
        botonIniciar.setEnabled(false);
    }
    
    /**
     * Método usado para iniciar la información paso a paso, solo puede ser llamado
     * después de hacer un recorrido.
     */
    private void pasoAPaso() {
        if(! botonPasoAPaso.isEnabled()) {
            return;
        }
        metro.iniciarPasoAPaso();
        botonPasoAPaso.setEnabled(false);
        botonAnterior.setEnabled(true);
        botonSiguiente.setEnabled(true);
    }
    
    /**
     * Método usado para avanzar entre estaciones, solo puede ser llamado después
     * de iniciar la información paso a paso.
     * 
     * @param mod Dirección en la que avanzará el recorrido.
     */
    private void metroMostrarSiguiente(int mod) {
        if(! botonSiguiente.isEnabled()) {
            return;
        }
        metro.mostrarSiguiente(mod);
    }
    
    /**
     * Método que cambia el valor de {@code mostrarNombres}, el cual dibujará, o
     * no, los nombres y líneas de metro de las Estaciones.
     */
    private void mostrarNombres() {
        metro.setMostrarNombres(mostrarNombres.isSelected());
    }
    
    /**
     * Método que inicia el recorrido de árbol de expansión mínima, solo puede ser
     * llamado si hay alguna Estación seleccionada.
     */
    private void iniciarPrim() {
        if(! botonPrim.isEnabled()) {
            return;
        }
        Estacion e = selected;
        reiniciar();
        metro.iniciarPrim(e);
        botonPasoAPaso.setEnabled(true);
    }
    
    /**
     * Método que inicia el recorrido de búsqueda primero en profundidad, solo
     * puede ser llamado si hay alguna Estación seleccionada.
     */
    private void iniciarDFS() {
        if(! botonDFS.isEnabled()) {
            return;
        }
        Estacion e = selected;
        reiniciar();
        metro.iniciarDFS(e);
        botonPasoAPaso.setEnabled(true);
    }
    
    /**
     * Método que inicia el recorrido de búsqueda primero en anchura, solo
     * puede ser llamado si hay alguna Estación seleccionada.
     */
    private void iniciarBFS() {
        if(! botonBFS.isEnabled()) {
            return;
        }
        Estacion e = selected;
        reiniciar();
        metro.iniciarBFS(e);
        botonPasoAPaso.setEnabled(true);
    }
    
    /**
     * Método que se llama cada vez que se presiona una tecla, usado para agregar
     * atajos de teclado para el uso de los botones.
     * 
     * @param evt Evento del teclado.
     */
    private void windowKeyPressed(KeyEvent evt) {
        switch(evt.getKeyCode()) {
            case 10: //prim
                iniciarPrim();
                break;
            case 37: //Flecha izquierda
                metroMostrarSiguiente(-1);
                break;
            case 39: //Flecha derecha
                metroMostrarSiguiente(1);
                break;
            case 38: //Flecha arriba
                cambiarZoom.setValue(constrainZoom(20));
                break;
            case 40: //Flecha abajo
                cambiarZoom.setValue(constrainZoom(-20));
                break;
            case 8: //Retroceso
            case 127: //Supr
                eliminarEstacion();
                break;
            case 65: //a
                agregarEstacion();
                break;
            case 66: //b
                iniciarBFS();
                break;
            case 68: //d
                iniciarDFS();
                break;
            case 72: //h
                ayuda();
                break;
            case 77: //m
                mostrarNombres.setSelected(! mostrarNombres.isSelected());
                mostrarNombres();
                break;
            case 80: //p
                pasoAPaso();
                break;
            case 82: //r
                reiniciar();
                break;
            case 83: //s
                iniciarRecorrido();
                break;
        }
    }
    
    /**
     * Método usado para restringir los valores del zoom, usado después de presionar
     * las teclas "Arriba" y "Abajo"; ya que estas, reducen el zoom en un valor 
     * específico.
     * 
     * @param mod Valor que determina si el valor del zoom crece o decrece.
     * @return El valor restringido del zoom.
     */
    private int constrainZoom(int mod) {
        if(cambiarZoom.getValue() + mod < cambiarZoom.getMinimum()) {
            return cambiarZoom.getMinimum();
        }
        if(cambiarZoom.getValue() + mod > cambiarZoom.getMaximum()) {
            return cambiarZoom.getMaximum();
        }
        return cambiarZoom.getValue() + mod;
    }
    
    /**
     * Método llamado al presionar el botón de ayuda.
     * 
     * @param evt Evento del mouse.
     */
    private void botonAyudaMousePressed(MouseEvent evt) {
        ayuda();
    }
    
    /**
     * Método llamado al presionar el botón de reiniciar.
     * 
     * @param evt Evento del mouse.
     */
    private void botonReiniciarMousePressed(MouseEvent evt) {
        reiniciar();
    }
    
    /**
     * Método llamado al presionar el botón de agregar.
     * 
     * @param evt Evento del mouse.
     */
    private void botonAgregarMousePressed(MouseEvent evt) {
        agregarEstacion();
    }
    
    /**
     * Método llamado al presionar el botón de iniciar.
     * 
     * @param evt Evento del mouse.
     */
    private void botonIniciarMousePressed(MouseEvent evt) {
        iniciarRecorrido();
    }
    
    /**
     * Método llamado al presionar el botón de paso a paso.
     * 
     * @param evt Evento del mouse.
     */
    private void botonPasoAPasoMousePressed(MouseEvent evt) {
        pasoAPaso();
    }
    
    /**
     * Método llamado al presionar el botón Siguiente y Anterior.
     * 
     * @param evt Evento del mouse.
     * @param mod Dirección en la que avanzará la Estación.
     */
    private void botonSiguienteMousePressed(MouseEvent evt, int mod) {
        metroMostrarSiguiente(mod);
    }
    
    /**
     * Método llamado al presionar la casilla de mostrar nombres.
     * 
     * @param e Evento del componente.
     */
    private void mostrarNombresItemStateChanged(ItemEvent e) {
        mostrarNombres();
    }
    
    /**
     * Método llamado al hacer un cambio al deslizador del zoom. Éste solo se actualiza
     * cuando es soltado.
     * 
     * @param evt Evento del componente.
     */
    private void cambiarZoomStateChanged(ChangeEvent evt) {
        double zoom = getZoom();
        zoomLabel.setText((int) (zoom * 100) + "%");
        if(cambiarZoom.getValueIsAdjusting()) {
            return;
        }
        metro.setZoom(zoom);
    }
    
    /**
     * Convierte el rango de valores(mínimo y máximo) del deslizador al rango de 
     * valores del zoom({@code metro.MIN_ZOOM} y {@code metro.MAX_ZOOM}).
     * 
     * @return El nuevo valor del zoom.
     */
    private double getZoom() {
        double proporcion = (double) (cambiarZoom.getValue() - cambiarZoom.getMinimum()) / (cambiarZoom.getMaximum() - cambiarZoom.getMinimum());
        double value = (proporcion * (Metro.MAX_ZOOM - Metro.MIN_ZOOM)) + Metro.MIN_ZOOM;
        return value;
    }
    
    /**
     * Método llamado al hacer clic en el botón de árbol de expansión mínima.
     * 
     * @param evt Evento del mouse
     */
    private void botonPrimMousePressed(MouseEvent evt) {
        iniciarPrim();
    }
    
    /**
     * Método llamado al hacer clic en el botón DFS.
     * 
     * @param evt Evento del mouse.
     */
    private void botonDFSMousePressed(MouseEvent evt) {
        iniciarDFS();
    }
    
    /**
     * Método llamado al hacer clic en el botón BFS.
     * 
     * @param evt Evento del mouse.
     */
    private void botonBFSMousePressed(MouseEvent evt) {
        iniciarBFS();
    }
    
    /**
     * Método main donde se crea y se muestra una nueva ventana.
     * 
     * @param args
     */
    public static void main(String[] args) {
        new Window();
    }
    
    /**
     * ScrollPane central que contendrá el metro.
     */
    private JScrollPane panelCentro;
    
    /**
     * Objeto {@code Metro} que manejará las funciones del grafo.
     */
    private Metro metro;
    
    
    /**
     * Panel que contendrá la lista de destinos / paradas, y la información del
     * recorrido.
     */
    private JPanel panelIzquierdo;
    
    
    /**
     * Elementos que estarán dentro del panel izquierdo.
     */
    private JLabel title;
    private InfoRecorrido panelInfo;
    
    /**
     * ScrollPane que contendrá el panel de estaciones.
     */
    private JScrollPane panelEstaciones;
    
    /**
     * Panel que contendrá la lista de estaciones.
     */
    private JPanel listaEstaciones;
    
    
    /**
     * ScrollPane que contendrá el panel de botones.
     */
    private JScrollPane panelAbajo;
    
    /**
     * Panel que contendrá los botones.
     */
    private JPanel panelBotones;
    
    /**
     * Botones que estarán dentro del panel de botones.
     */
    private JButton botonAyuda;
    private JButton botonReiniciar;
    private JButton botonAgregar;
    private JButton botonEliminar;
    private JButton botonIniciar;
    private JButton botonPasoAPaso;
    private JButton botonAnterior;
    private JButton botonSiguiente;
    private JCheckBox mostrarNombres;
    private JSlider cambiarZoom;
    private JLabel zoomLabel;
    private JButton botonPrim;
    private JButton botonDFS;
    private JButton botonBFS;

}
