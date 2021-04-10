/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienteAfectivo;

import clienteRacional.AgenteRacional_v2;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import ontology.Producto;
import subastador.Subastador_v2;

/**
 *
 * @author ferta
 */
public class AgenteAfectivoGUI extends JFrame {

    private JPanel contentPane;
    @SuppressWarnings("unused")
    private HashMap<Producto, String> productos;
    private ModeloTabla model;
    private JTable table;
    private JLabel lblwallet;
    private JTextField title;
    private int wallet;
    private JTextField maxPrice;
    private AgenteAfectivo_v2 pujador;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AgenteAfectivoGUI frame = new AgenteAfectivoGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public AgenteAfectivoGUI() {
       
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                pujador.takeDown();
            }
        });

        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 70, getWidth() - 20, getHeight() - 80);
        contentPane.add(scrollPane);
        
     
        
        lblwallet = new JLabel("Billetera:  "+ wallet);
        lblwallet.setBounds(10, 20, 100, 44);
        contentPane.add(lblwallet);

        table = new JTable();
        scrollPane.setViewportView(table);

    }

    public AgenteAfectivoGUI(HashMap<Producto, String> productos, AgenteAfectivo_v2 pujador) {
        this();
        this.productos = productos;
        this.pujador = pujador;
        model = new ModeloTabla(productos);
        this.wallet = pujador.getWallet();
        
        table.setModel(model);
        setTitle(pujador.getLocalName());
    }

    public ModeloTabla getModel() {
        return model;
    }
    
       public void updateWallet(int wallet) {
               lblwallet.setText("Billetera :"+ wallet);
       
    }

}
