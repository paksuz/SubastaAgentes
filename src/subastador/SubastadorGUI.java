/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subastador;

import java.awt.EventQueue;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import ontology.Producto;

/**
 *
 * @author ferta
 */
public class SubastadorGUI extends JFrame {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SubastadorGUI frame = new SubastadorGUI();
                    frame.setVisible(true);
                    SubastadorGUI.main(args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private JPanel contentPane;
    private JTextField titulo;
    private JTextField precio;
    private JTextField incremento;
    private Subastador_v2 subastador;
    private JLabel lblTitulo;
    private ModeloTabla model;
    private JTable table;

    public SubastadorGUI() {
        setTitle("Agente Subastador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
       
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, getWidth() - 20, getHeight() - 80);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);
        

    }
    public SubastadorGUI(ArrayList<Producto> productos, Subastador_v2 subastador) {
		this();
		this.subastador = subastador;
		model = new ModeloTabla(productos);
		table.setModel(model);
	}

	public ModeloTabla getModel() {
		return this.model;
	}

}
