package subastador;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import ontology.ReadCsv;
import ontology.Producto;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ontology.Cliente;
import ontology.OntologiaSubasta;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ferta
 */
@SuppressWarnings("serial")
public class Subastador_v2 extends Agent {

    private ArrayList<Producto> productos;
    String ruta = "C:\\Users\\ferta\\Desktop\\u\\Tesis\\JadeEjemplos\\TesisFinal\\src\\datos_in\\subasta_15.csv";
    private Ontology ontology = OntologiaSubasta.getInstance();
    private Codec codec = new SLCodec();
    private boolean flag;
    private int i;
    private SubastadorGUI subastadorGUI;

    protected void setup() {
       
        productos = ReadCsv.LeerProductos(ruta);
        System.out.println("Hola!" + getAID().getName() + " esta Listo!");
//        csnl.flush();
        
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
        subastadorGUI = new SubastadorGUI(productos, this);
		subastadorGUI.setVisible(true);

        addBehaviour(new SubastadorBehaviour(this, 1000, productos.get(0), 0, productos));
        
        /*  for(Producto p: productos){
            
             addBehaviour(new SubastadorBehaviour(this, 1000, p,0,productos));
        
        
        }*/
    }

    public void takeDown() {
        try {
            DFService.deregister(this);

        } catch (FIPAException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(getAID().getName() + " ha terminado");
    }

    public Ontology getOntology() {
        return ontology;
    }

    public Codec getCodec() {
        return codec;
    }

    public void actualizarGanador(Producto producto, String ganador) {
        int i = 0;
        int temp = 0;
        for (Producto p : productos) {
            if (p.equals(producto)) {
                temp = i;
            }
            i++;
        }
        subastadorGUI.getModel().changeStatus(producto);
        productos.get(temp).setGanador(ganador);
    }
    	public void updatePrice(Producto producto) {
		subastadorGUI.getModel().changeStatus(producto);
	}
}
