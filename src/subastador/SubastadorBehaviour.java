package subastador;

import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import subastador.Subastador_v2;
import jade.content.onto.basic.Action;
import ontology.Producto;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLCodec.CodecException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.Console;
import static java.lang.System.currentTimeMillis;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import ontology.CallForProposal;
import ontology.AceptarPropuesta;
import ontology.Cliente;
import ontology.RechazarPropuesta;
import ontology.Informar;
import ontology.Propuesta;
import ontology.Resumen;
import ontology.Solicitud;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ferta
 */
public class SubastadorBehaviour extends TickerBehaviour {

    private Producto producto;
    private ArrayList<AID> clientes;
    private AID ganador;
    private Subastador_v2 subastador;
    private int posicion;
    private ArrayList<Producto> subasta;
    private ArrayList<Cliente> Resultados;
    private boolean flg = true;

    public SubastadorBehaviour(Agent a, int period, Producto producto, int pos, ArrayList<Producto> subasta) {
        super(a, period);
        this.producto = producto;
        this.subastador = (Subastador_v2) a;
        this.subasta = subasta;
        this.posicion = pos + 1;
        this.Resultados = new ArrayList<Cliente>();
    }

    public void setResultados(Cliente c) {
        this.Resultados.add(c);
    }

    @Override
    protected void onTick() {
       Console csnl = System.console();
       
      //  Console csnl = System.console();
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Cliente");
        template.addServices(sd);
        boolean hayGanador = false;
        

        try {
            if(flg){
                DFAgentDescription[] result = DFService.search(myAgent, template);
                System.out.println("Busqueda Realizada");
                clientes = new ArrayList<>();
                for (DFAgentDescription ad : result) {
                    clientes.add(ad.getName());
                }
                flg=false;
            }

            if (!clientes.isEmpty()) {
                Collections.shuffle(clientes);
                CallForProposal cfp = new CallForProposal(producto);

                ACLMessage message = new ACLMessage(ACLMessage.CFP);
                message.setOntology(subastador.getOntology().getName());
                message.setLanguage(subastador.getCodec().getName());
                try {
                    myAgent.getContentManager().fillContent(message, new Action(myAgent.getAID(), cfp));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                message.setSender(myAgent.getAID());
                for (AID c : clientes) {
                    message.addReceiver(c);
                }
                message.setConversationId(producto.getNombre());
                message.setReplyWith("Interesado" + currentTimeMillis());
                myAgent.send(message);
                MessageTemplate mt = MessageTemplate.and(
                        MessageTemplate.MatchConversationId(producto.getNombre()),
                        MessageTemplate.MatchInReplyTo(message.getReplyWith()));
                mt = MessageTemplate.and(mt, MessageTemplate.MatchOntology(subastador.getOntology().getName()));
                int contadorrespuestas = 0;
                int contadorInteresados = 0;
                
                do {
                    ACLMessage respuesta = myAgent.receive(mt);
                    if (respuesta != null) {
                        Action action = null;
                        try {
                            action = (Action) subastador.getContentManager().extractContent(respuesta);

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        Propuesta propuesta = (Propuesta) action.getAction();

                        if (propuesta.getAnswer() == true) {

                            if (!hayGanador) {
                                hayGanador = true;

                                ganador = action.getActor();

                            }

                            System.out.println(action.getActor().getLocalName() + " acepta pujar por"
                                    + message.getConversationId() + " por "
                                    + producto.getPrecio() + "dolares");
                            contadorInteresados++;
                        }
                        contadorrespuestas++;
                    } else {
                        block();
                    }

                } while (contadorrespuestas < clientes.size());

                AceptarPropuesta aceptarPropuesta = new AceptarPropuesta(producto);
                message = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                message.setOntology(subastador.getOntology().getName());
                message.setLanguage(subastador.getCodec().getName());
                message.setSender(myAgent.getAID());
                message.addReceiver(ganador);
                try {
                    myAgent.getContentManager().fillContent(message, new Action(myAgent.getAID(), aceptarPropuesta));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                message.setConversationId(producto.getNombre());
                myAgent.send(message);

                RechazarPropuesta rechazarPropuesta = new RechazarPropuesta(producto);
                message = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                message.setOntology(subastador.getOntology().getName());
                message.setLanguage(subastador.getCodec().getName());
                message.setSender(myAgent.getAID());
                message.setConversationId(producto.getNombre());
                for (AID c : clientes) {
                    if (!c.equals(ganador)) {
                        message.addReceiver(c);
                    }
                }

                try {
                    myAgent.getContentManager().fillContent(message, new Action(myAgent.getAID(), rechazarPropuesta));

                } catch (Exception e) {
                    System.out.println("error en rechazar propuesta");
                }
                myAgent.send(message);

                boolean haTerminado = false;

                if (contadorInteresados == 0) {
                    //nadie se interesa por la subasta
                    if (ganador != null) {
                        System.out.println("Subasta de :" + producto.getNombre() + " Ganada Por :" + ganador.getLocalName() + ", a un precio de : " + producto.getPrecio());
                        haTerminado = true;

                    } else {
                        System.out.println("A las 1...");
                        System.out.println("A las 2...");
                        System.out.println("A las 3...");
                        System.out.println("Al agua!");
                        subastador.actualizarGanador(producto, null);

                        if (posicion < subasta.size()) {
                            myAgent.addBehaviour(new SubastadorBehaviour(subastador, 1000, subasta.get(posicion), posicion, subasta));
                            this.stop();
                        } else {

                            //  System.out.println(subastador.getClientes()!=null);
                            System.out.println("TERMINANDO");
                            /*   ACLMessage msg = new ACLMessage(ACLMessage.AGREE);
                            Resumen r = new Resumen(new Cliente(myAgent.getName(),69));
                            msg.setLanguage(subastador.getCodec().getName());
                            msg.setOntology(subastador.getOntology().getName());
                            try{
                                myAgent.getContentManager().fillContent(msg, new Action(myAgent.getAID(),r));
                                for(AID c : clientes){
                                    msg.addReceiver(c);
                                }
                                myAgent.send(msg);
                            }catch(Exception e){
                                e.printStackTrace();
                            }*/

                            //   System.out.println(subastador.getClientes() != null);
                            myAgent.addBehaviour(new SubastadorInforme(subasta, clientes, subastador));
                            subastador.actualizarGanador(producto, ganador.getLocalName());
                            this.stop();
                        }

                    }
                } else if (contadorInteresados == 1) {
                    //solo hay un interesado en la ronda actual
                    System.out.println("Subasta de :" + producto.getNombre() + " Ganada por :" + ganador.getLocalName() + "   al ser el unico interesado");
                    haTerminado = true;
                } else {
                    
                    producto.actualizarPrecio();
                    subastador.updatePrice(producto);
                }

                if (haTerminado == true) {

                    //se informa primero alos q no ganaron
                    Informar inform = new Informar(producto);
                    message = new ACLMessage(ACLMessage.INFORM);
                    message.setOntology(subastador.getOntology().getName());
                    message.setLanguage(subastador.getCodec().getName());
                    message.setSender(myAgent.getAID());
                    message.setConversationId(producto.getNombre());
                    for (AID c : clientes) {
                        if (!c.equals(ganador)) {
                            message.addReceiver(c);
                        }
                    }
                    try {
                        myAgent.getContentManager().fillContent(message,
                                new Action(myAgent.getAID(), inform));
                    } catch (Codec.CodecException | OntologyException e) {
                        e.printStackTrace();
                    }
                    myAgent.send(message);

                    /*  **************************************************************************************************/
 /*   Pediremos al agente el pago del objeto
        
                
        
                     */ if (producto.getFlag()) {
                        producto.setPrecio((int) (producto.getPrecio() / producto.getIncremento()));
                    }

                    Solicitud solicitud = new Solicitud(producto);
                    message = new ACLMessage(ACLMessage.REQUEST);
                    message.setOntology(subastador.getOntology().getName());
                    message.setLanguage(subastador.getCodec().getName());
                    message.setSender(myAgent.getAID());
                    message.setConversationId(producto.getNombre());
                    message.addReceiver(ganador);
                    try {
                        myAgent.getContentManager().fillContent(message, new Action(myAgent.getAID(), solicitud));

                    } catch (Exception e) {
                        System.out.println("error en solicitud de pago");
                    }
                    myAgent.send(message);

                    subastador.actualizarGanador(producto, ganador.getLocalName());
                    if (posicion < subasta.size()) {
                        System.out.println(posicion);
                        myAgent.addBehaviour(new SubastadorBehaviour(subastador, 1000, subasta.get(posicion), posicion, subasta));
                        this.stop();
                    } else {

                        System.out.println("TERMINANDO");

                        myAgent.addBehaviour(new SubastadorInforme(subasta, clientes, subastador));
                        subastador.actualizarGanador(producto, ganador.getLocalName());
                        this.stop();

                    }

                }

            }
        } catch (FIPAException e) {
            System.out.println(e.getMessage());
        }
        
    }

    
}
