package clienteRacional;

import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLCodec.CodecException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import static java.lang.System.currentTimeMillis;
import java.util.HashMap;
import ontology.AceptarPropuesta;
import ontology.CallForProposal;
import ontology.Cliente;
import ontology.Informar;
import ontology.Producto;
import ontology.Propuesta;
import ontology.RechazarPropuesta;
import ontology.Resumen;
import ontology.Solicitud;

/**
 *
 * @author ferta
 */
public class AgenteRacionalBehaviour extends CyclicBehaviour {

    private HashMap<Producto, String> objetivos;
    private AgenteRacional_v2 cliente;
    private int wallet;

    public AgenteRacionalBehaviour(AgenteRacional_v2 cliente, HashMap<Producto, String> objetivos, int wallet) {
        super();
        this.objetivos = new HashMap<Producto, String>(objetivos);
        this.cliente = cliente;
        this.wallet = wallet;

    }

    @Override
    public void action() {
        cliente.updateWallet(wallet);
        Action action = null;
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.CFP),
                MessageTemplate.MatchOntology(cliente.getOntology().getName()));
        ACLMessage message = null;
        ACLMessage reply = null;
        int maxPrice = (int) 0;
        String estado = "Adquirido";

        message = myAgent.receive(mt);
        if (message != null) {
            try {
                action = (Action) cliente.getContentManager().extractContent(message);

            } catch (Exception e) {

                e.printStackTrace();

            }
            CallForProposal cfp = (CallForProposal) action.getAction();
            String nombre = cfp.getProducto().getNombre();
            int precio = cfp.getProducto().getPrecio();
            reply = message.createReply();
            reply.setPerformative(ACLMessage.PROPOSE);
            reply.setOntology(cliente.getOntology().getName());
            reply.setLanguage(cliente.getCodec().getName());

            Propuesta proposal = new Propuesta();
            proposal.setProducto(cfp.getProducto());

            if (precio >= 1 && precio != 0) {
               cliente.changeStatus(new Producto(nombre), "En curso");
                maxPrice = 0;
                Boolean flag = false;
                //revisamos si esta dentro de nuestros objetivos
                for (Producto p : objetivos.keySet()) {
                    if (p.getNombre().equals(nombre)) {
                        
                        maxPrice = p.getPrecio() ;
                        estado = objetivos.get(p);
                        flag = true;
                    }
                }
                //pujamos
                if ((flag == true) && (precio <= maxPrice) && (estado != "Adquirido") && (wallet - precio >= 0)) {
                    proposal.setAnswer(new Boolean(true));
                    for(Producto p : objetivos.keySet()){
                        if(p.getNombre().equals(nombre)){
                            p.setEstado("PUJANDO");
                        }
                    }
                    
                    System.out.println(myAgent.getLocalName()
                            + ": acepto pujar por " + nombre + " por " + precio);
                } else if ((flag == true) && (precio > maxPrice)) {//no pujamos
                    proposal.setAnswer(new Boolean(false));
                    setEstado(objetivos,"pujando",proposal.getProducto());
                   // cambiarEstado("Fuera de presupuesto");
                    System.out.println(myAgent.getLocalName()
                            + ": rechazo pujar por " + nombre + " por " + precio + " Supera mi presupuesto");
                    estado = "Retirado";
                     setEstado(objetivos, "Precio esperado Superado",proposal.getProducto());
                    cliente.changeStatus(cfp.getProducto(), "Precio superado");
                } else {
                    proposal.setAnswer(new Boolean(false));
                    System.out.println(myAgent.getLocalName() + ": " + nombre + " no esta dentro de mis intereses");
                    estado = "No Interesado";
                    setEstado(objetivos, "No me Interesa",proposal.getProducto());
                }

            } else {
                reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                reply.setContent("Precio no Valido");
            }
            //enviamos nuestra respuesta
            try {
                cliente.getContentManager().fillContent(reply,
                        new Action(myAgent.getAID(), proposal));
            } catch (Exception e) {
                e.printStackTrace();
            }
            reply.setSender(myAgent.getAID());
            myAgent.send(reply);
        }
        mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        message = myAgent.receive(mt);
        if (message != null) {
            try {
                action = (Action) cliente.getContentManager().extractContent(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
            AceptarPropuesta aceptarPropuesta = (AceptarPropuesta) action.getAction();
            Producto producto = aceptarPropuesta.getProducto();
            cliente.changeStatus(producto, "Ronda Ganada");
            setEstado(objetivos, "Ronda Ganada!",producto);

        } else {
            block();
        }

        mt = MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL);
        message = myAgent.receive(mt);
        if (message != null) {
            try {
                action = (Action) cliente.getContentManager().extractContent(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
            RechazarPropuesta rejectProposal = (RechazarPropuesta) action.getAction();
            Producto producto = rejectProposal.getProducto();
            cliente.changeStatus(producto, "Ronda no ganada");
            setEstado(objetivos, "Ronda Perdida",producto);
            
        } else {
            block();
        }

        mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        message = myAgent.receive(mt);
        if (message != null) {
            //Gano la subasta
            try {
                action = (Action) cliente.getContentManager().extractContent(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Solicitud request = (Solicitud) action.getAction();
            wallet = wallet - request.getProducto().getPrecio();
            cliente.updateWallet(wallet);
            System.out.println("HE GANADO! mi:" + request.getProducto() + " y me quedan :" + wallet);
            
          //  objetivos.remove(request.getProducto());
            setEstado(objetivos, "Ganada!!",request.getProducto());
            cliente.changeStatus(request.getProducto(), "Adquirido");
        } else {
            block();
        }

        mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        message = myAgent.receive(mt);
        if (message != null) {
            try {
                action = (Action) cliente.getContentManager().extractContent(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
            Informar inform = (Informar) action.getAction();
            if (objetivos.keySet().contains(inform.getProducto())) {
                cliente.changeStatus(new Producto(message.getConversationId()), "No ganada");
            }
            setEstado(objetivos, "Perdida",inform.getProducto() );
            System.out.println(myAgent.getLocalName() + ": no he ganado la subasta de " + message.getConversationId());
        }

        mt = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
        message = myAgent.receive(mt);
        if (message != null) {
            try {
                action = (Action) cliente.getContentManager().extractContent(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
            Resumen r = (Resumen) action.getAction();
            r.setCliente(new Cliente(myAgent.getLocalName(), wallet));
            message = message.createReply();
            message.setPerformative(ACLMessage.INFORM_REF);
            message.setOntology(cliente.getOntology().getName());
            message.setLanguage(cliente.getCodec().getName());
            try {
                cliente.getContentManager().fillContent(message,
                        new Action(myAgent.getAID(), r));
            } catch (Exception e) {
                e.printStackTrace();
            }
            message.setSender(myAgent.getAID());
            myAgent.send(message);

        }
        mt = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
        message = myAgent.receive(mt);
        if (message != null) {
           // System.out.println("recibi");
            try {
                action = (Action) cliente.getContentManager().extractContent(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
            Resumen r = (Resumen) action.getAction();
           // System.out.println("Respondiendo");
            r.setCliente(new Cliente(myAgent.getLocalName(), wallet));
            ACLMessage respuesta = new ACLMessage(ACLMessage.INFORM_IF);
            respuesta = message.createReply();
            respuesta.setPerformative(ACLMessage.PROPOSE);
            respuesta.setOntology(cliente.getOntology().getName());
            respuesta.setLanguage(cliente.getCodec().getName());
            try {
                cliente.getContentManager().fillContent(respuesta,
                        new Action(myAgent.getAID(), r));
            } catch (Exception e) {
                e.printStackTrace();
            }

            respuesta.setSender(myAgent.getAID());
            myAgent.send(respuesta);

        } else {
            block();
        }

    }
    public void setEstado(HashMap<Producto,String> objetivos,String estado,Producto b ){
        
        String aux = b.getNombre();
        for(Producto p : objetivos.keySet()){
            if(p.getNombre().equals(aux)){
                p.setEstado(estado);
                cliente.updateGUI(p);
                
            }
           
            
        }
        
    }

}
