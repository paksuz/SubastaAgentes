
package clienteAfectivo;

import clienteRacional.AgenteRacionalGUI;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.HashMap;
import ontology.OntologiaSubasta;
import ontology.Producto;
import ontology.ReadCsv;

/**
 *
 * @author ferta
 */
public class AgenteAfectivo_v2 extends Agent {
    
    private HashMap<Producto,String> objetivos;
    private int wallet;
    private Ontology ontology = OntologiaSubasta.getInstance();
    private Codec codec = new SLCodec();;
    private AgenteAfectivoGUI pujadorGUI;
    private String ruta = "./src/datos_in/subasta_15AgenteAfectivo.csv";
    
    public void setup(){
        
        wallet = 1000;
        objetivos = ReadCsv.LeerObjetivosAfectivos(ruta);
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Cliente");
        sd.setName("Agente Racional-Jade");
        dfd.addServices(sd);
        try{
            DFService.register(this, dfd);
        }catch(FIPAException e){
            System.out.println(e.getMessage());
        }
        
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
        
        for(Producto producto : objetivos.keySet()){
            System.out.println("Pujando por "+ producto.getNombre()+ " a un maximo de " + producto.getPrecio()*3+ " Dolares");
        }
        addBehaviour(new AgenteAfectivoBehaviour(this, objetivos,wallet));
        pujadorGUI = new AgenteAfectivoGUI(objetivos, this);
        
        pujadorGUI.setVisible(true);
    }
    
    public void takeDown(){
        try{
            DFService.deregister(this);
            
        }catch(FIPAException e){
            System.out.println(e.getMessage());
        }
        System.out.println(getAID().getName() + " ha terminado");
    }    
    
    
    public void setWallet(int dinero){
        this.wallet = dinero;
    }
    public int getWallet(){
        return wallet;
    }
    public void comprarObjeto(int precio){
        this.wallet = wallet - precio;
    }
    public boolean puedoComprar(int precio){
        return (wallet-precio>1);
    }
    public Ontology getOntology(){
        return ontology;
    }
    public Codec getCodec(){
        return codec;
    }
    
    public void changeStatus(Producto producto, String status) {
        if (objetivos.containsKey(producto)&&  objetivos.get(producto) != "Adquirido") {
            objetivos.put(producto, status);
         //   pujadorGUI.getModel().changeStatus(producto, status);
        }
    }
    
    public void updateGUI(Producto p){
        pujadorGUI.getModel().changeStatus(p);
        
    }
    public void updateWallet(int wallet){
        pujadorGUI.updateWallet(wallet);
    }
}
