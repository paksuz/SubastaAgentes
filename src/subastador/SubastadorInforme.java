package subastador;

import clienteAfectivo.AgenteAfectivo_v2;
import clienteRacional.AgenteRacionalBehaviour;
import clienteRacional.AgenteRacional_v2;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ontology.Cliente;
import ontology.Producto;
import ontology.Resumen;

/**
 *
 * @author ferta
 */
public class SubastadorInforme extends OneShotBehaviour {

    private ArrayList<Producto> subasta;
    private ArrayList<AID> clientes;
    private AgenteRacionalBehaviour ar;
    private Subastador_v2 subastador;

  

    public SubastadorInforme(ArrayList<Producto> subasta, ArrayList<AID> clientes, Subastador_v2 s) {
        this.subasta = subasta;
        this.clientes = clientes;
        this.subastador = s;
    }

    @Override
    public void action() {
        ArrayList<Cliente> resultados = new ArrayList<Cliente>();
        int aux = 0;
        Action action = null;
        ACLMessage msg = new ACLMessage(ACLMessage.AGREE);
        Resumen r = new Resumen(new Cliente(myAgent.getName(), 69));
        msg.setLanguage(subastador.getCodec().getName());
        msg.setOntology(subastador.getOntology().getName());
        msg.setConversationId("reportando");
        msg.setReplyWith("reportando");
        try {
            myAgent.getContentManager().fillContent(msg, new Action(myAgent.getAID(), r));
            for (AID c : clientes) {
                msg.addReceiver(c);
            }
            myAgent.send(msg);
            //   System.out.println("envie" );
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println("Antes del DO");
         MessageTemplate mt = MessageTemplate.and(
                        MessageTemplate.MatchConversationId(msg.getConversationId()),
                        MessageTemplate.MatchInReplyTo(msg.getReplyWith()));
                mt = MessageTemplate.and(mt, MessageTemplate.MatchOntology(subastador.getOntology().getName()));
            
        do {
        //    System.out.println("PRIMERA EJECUCION do");
            ACLMessage message = myAgent.receive(mt);
            if (message != null) {
            //    System.out.println("MENSAJE DISTINTO DE NULL");
                try {
                    action = (Action) myAgent.getContentManager().extractContent(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Resumen rr = (Resumen) action.getAction();
                
                resultados.add(rr.getCliente());
                // System.out.println(rr.getCliente().getWallet());
                
                // System.out.println(temporal.getNombre()+temporal.getWallet());-                                  // setResultados(temporal);
                // resultadosAgentes.add(rr.getCliente());
                  aux++;

            }else{
                block();
            
        }
          
        } while (aux < clientes.size());
        System.out.println("Resultados Subasta");
        for (Producto p : subasta) {
            System.out.println("Nombre: " + p.getNombre() + " Precio: " + p.getPrecio() + " Ganador: " + p.getGanador());

        }
        System.out.println("Saldo de los Agentes:");
        // System.out.println("prueba" +ar.)
        for (Cliente c : resultados) {
            System.out.println(c.getNombre() + "   Dinero:" + c.getWallet());
        }

    }

}
