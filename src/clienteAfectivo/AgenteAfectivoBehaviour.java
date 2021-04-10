package clienteAfectivo;

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
public class AgenteAfectivoBehaviour extends CyclicBehaviour {

    private HashMap<Producto, String> objetivos;
    private AgenteAfectivo_v2 cliente;
    private int wallet;
    private AgenteAfectivo_v2 agenteAfectivo;

    public AgenteAfectivoBehaviour(AgenteAfectivo_v2 cliente, HashMap<Producto, String> objetivos, int wallet) {
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
        String estado = "No Adquirido";
        double factor = 0.0;

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
            reply.setPerformative(ACLMessage.INFORM_IF);
            reply.setOntology(cliente.getOntology().getName());
            reply.setLanguage(cliente.getCodec().getName());

            Propuesta proposal = new Propuesta();
            proposal.setProducto(cfp.getProducto());

            if (precio >= 1 && precio != 0) {

                maxPrice = 0;
                Boolean flag = false;
                //revisamos si esta dentro de nuestros objetivos
                for (Producto p : objetivos.keySet()) {
                    if (p.getNombre().equals(nombre)) {

                        maxPrice = reaccionSomatica(factor, p.getPrecio() );
                        estado = objetivos.get(p);
                        flag = true;
                        factor = getFactorSomatico(p);
                        //  System.out.println("mi reaccion hacia "+nombre+" es de : "+factor);
                        //  maxPrice = reaccionSomatica(factor,maxPrice);
                    }
                }
                //pujamos
                maxPrice = reaccionSomatica(factor, maxPrice);
                if ((flag == true) && (precio <= maxPrice) && (estado != "Pujando") && (wallet - precio >= 0)) {
                    proposal.setAnswer(new Boolean(true));
                    setEstado(objetivos,"pujando",proposal.getProducto());
                    estado = "Pujando";
                    System.out.println(myAgent.getLocalName()
                            + ": acepto pujar por " + nombre + " por " + precio);
                } else if ((flag == true) && (precio > maxPrice) || (wallet - precio < 0)) {//no pujamos
                    proposal.setAnswer(new Boolean(false));
                    System.out.println(myAgent.getLocalName()
                            + ": rechazo pujar por " + nombre + " por " + precio + " Supera mi presupuesto");
                    cliente.changeStatus(cfp.getProducto(), "Precio superado");
                     setEstado(objetivos, "Precio esperado Superado",proposal.getProducto());
                } else {
                    proposal.setAnswer(new Boolean(false));
                    System.out.println(myAgent.getLocalName() + ": " + nombre + " no esta dentro de mis intereses");
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
            System.out.println("HE GANADO! mi:" + request.getProducto() + " y me quedan : " + wallet);
          //  objetivos.remove(request.getProducto());
            cliente.updateWallet(wallet);
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
         //   System.out.println("recibi");
            try {
                action = (Action) cliente.getContentManager().extractContent(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
            Resumen r = (Resumen) action.getAction();
         //   System.out.println("Respondiendo");
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

        }else{
            
            block();
        }

    }

    public double getFactorSomatico(Producto p) {

        int aux = p.getFactor();
        double factor = (double) Math.atan(aux);

        return factor;

    }

    public int reaccionSomatica(double factor, int precioMax) {

        int reaccion = 0;
        if (factor >= 0.5) {
            reaccion = (int) (precioMax * 1.4); //reaccion positiva
        } else if (factor < 0.5 && factor >= -0.5) {
            //reaccion neutra
            reaccion = precioMax;
        } else if (factor < -0.5) {
            reaccion = (int) (precioMax * 0.7);
            //reaccion mala
        }
        return reaccion;
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
