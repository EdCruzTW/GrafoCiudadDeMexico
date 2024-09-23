/**
 * Integrantes:
 * CERVANTES CRUZ EDGAR
 * GARCIA TRINIDAD EMIR ESTEBAN
 * LOPEZ DEL CASTILLO DANIEL ISAY
 * PEREZ MENDIETA ALDO
*/

package extra;

import clases.Estacion;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;

/**
 * Clase que permite mostrar el nombre de una {@code Estacion} en una interfaz
 * gráfica, utilizando un {@code JLabel}.
 * 
 */
public class PanelEstacion extends RoundPanel {
    
    /**
     * {@code Estacion} que se mostrará en pantalla.
     */
    private Estacion estacion;
    
    /**
     * Crea un Panel con la {@code Estacion} especificada.
     * 
     * @param estacion La estación que se mostrará en pantalla
     */
    public PanelEstacion(Estacion estacion) {
        super(18);
        this.estacion = estacion;
        initComponents();
    }

    /**
     * Dar formato al panel, agregar un {@code JLabel} en el cual se mostrará el
     * nombre de la estación.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Colors.DIJKSTRA_COLOR);
        setPreferredSize(new Dimension(0, 65));
        JLabel label = new JLabel(estacion.getNombre());
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font("Century Gothic", 0, 14));
        add(label, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Devuelve la {@code Estacion} que contiene.
     * 
     * @return La {@code Estacion} que contiene
     */
    public Estacion getEstacion() {
        return estacion;
    }
    
}
