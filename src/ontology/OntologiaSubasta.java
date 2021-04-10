package ontology;


import jade.content.AgentAction;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PrimitiveSchema;
import ontology.AceptarPropuesta;
import ontology.CallForProposal;
import ontology.Informar;
import ontology.Producto;
import ontology.Propuesta;
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
public class OntologiaSubasta extends Ontology {
    
    //elnombre que identifica la ontologia
    public static final String ONTOLOGY_NAME = "Ontologia-Subasta";
    
    //vocabulario
    public static final String PRODUCTO = "producto";
    public static final String NOMBRE ="nombre";
    public static final String PRECIO = "precio";
    public static final String INCREMENTO = "incremento";
    public static final String GANADOR = "ganador";
    public static final String CFP = "CallForProposal";
    public static final String PROPUESTA = "Propuesta";
    public static final String RESPUESTA = "answer";
    public static final String ACEPTAR_PROPUESTA = "AceptarPropuesta";
    public static final String RECHAZAR_PROPUESTA = "RejectPropuesta";
    public static final String SOLICITUD = "Solicitud";
    public static final String INFORMAR = "Informar";
    public static final String RESUMEN = "Resumen";
    public static final String WALLET = "wallet";
    public static final String CLIENTE = "Cliente";
    
    //instanciamos el singleton de la ontologia
    private static OntologiaSubasta laInstancia = new OntologiaSubasta();
    
    public static OntologiaSubasta getInstance(){
        return laInstancia;
    }
    
    
    
    private OntologiaSubasta(){
        super(ONTOLOGY_NAME, BasicOntology.getInstance());
        
        
        try{
            add(new ConceptSchema(PRODUCTO), Producto.class);
            ConceptSchema cs = (ConceptSchema) getSchema(PRODUCTO);
            cs.add(NOMBRE,(PrimitiveSchema) getSchema(BasicOntology.STRING));
            cs.add(PRECIO, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
            
            
            
            add(new AgentActionSchema(CFP), CallForProposal.class);
            AgentActionSchema schema =(AgentActionSchema) getSchema(CFP);
            schema.add(PRODUCTO, (ConceptSchema) getSchema(PRODUCTO));
            
            
            add(new AgentActionSchema(PROPUESTA), Propuesta.class);
            schema =(AgentActionSchema) getSchema(PROPUESTA);
            schema.add(RESPUESTA, (PrimitiveSchema) getSchema(BasicOntology.BOOLEAN));
            schema.add(PRODUCTO, (ConceptSchema) getSchema(PRODUCTO));
            

            
            add(new AgentActionSchema(RECHAZAR_PROPUESTA), RechazarPropuesta.class);
            schema = (AgentActionSchema) getSchema(RECHAZAR_PROPUESTA);
            schema.add(PRODUCTO,(ConceptSchema) getSchema(PRODUCTO));
            
            add(new AgentActionSchema(ACEPTAR_PROPUESTA), AceptarPropuesta.class);
            schema = (AgentActionSchema) getSchema(ACEPTAR_PROPUESTA);
            schema.add(PRODUCTO,(ConceptSchema) getSchema(PRODUCTO));
            
            add(new AgentActionSchema(SOLICITUD), Solicitud.class);
            schema = (AgentActionSchema) getSchema(SOLICITUD);
            schema.add(PRODUCTO, (ConceptSchema) getSchema(PRODUCTO));
            
            add(new AgentActionSchema(INFORMAR), Informar.class);
            schema = (AgentActionSchema) getSchema(INFORMAR);
            schema.add(PRODUCTO, (ConceptSchema) getSchema(PRODUCTO));
            
            add(new ConceptSchema(CLIENTE),Cliente.class);
            cs = (ConceptSchema) getSchema(CLIENTE);
            cs.add(NOMBRE,(PrimitiveSchema) getSchema(BasicOntology.STRING));
            cs.add(WALLET, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
            
            add(new AgentActionSchema(RESUMEN), Resumen.class);
            schema = (AgentActionSchema) getSchema(RESUMEN);
            schema.add(CLIENTE, (ConceptSchema) getSchema (CLIENTE));
            
            
           
            
            
            
            
            
        } catch(OntologyException oe) {
            System.out.println(oe.getMessage());
            
        }
    }
}
