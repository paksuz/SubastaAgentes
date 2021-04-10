import subastador.Subastador_v2;
import clienteAfectivo.AgenteAfectivo_v2;
import clienteRacional.AgenteRacional_v2;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import java.io.Console;

public class MiAgente extends Agent {
    
    protected void setup(){
        System.out.println("Bienvenido a Jade Subasta");
        System.out.println("Inicializando todo ....");
        
        System.out.println("Generando agentes");
        addBehaviour(new GenerarAgentes(this));
        
    }

    
    class GenerarAgentes extends OneShotBehaviour{

        GenerarAgentes(Agent a){
            super(a);
        }
        
        public void action(){
            
            String subastadorID;
            String clienteID;
           // Console csn = System.console();
            try{
                jade.wrapper.AgentController ref;

                ref = myAgent.getContainerController().acceptNewAgent("Subastador",new Subastador_v2());
                ref.start(); 
                ref = myAgent.getContainerController().acceptNewAgent("Agente Racional",new AgenteRacional_v2());
                ref.start(); 
               ref = myAgent.getContainerController().acceptNewAgent("Agente Afectivo",new AgenteAfectivo_v2());
                ref.start(); 
                
           //     csn.flush();
                
            }catch(Exception e){
                System.out.println("Error en añadir agentes al container!");
            }

            blockingReceive();            
            

        } 

        
        
    }

}

