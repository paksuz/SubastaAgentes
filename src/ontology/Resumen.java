/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ontology;

import jade.content.AgentAction;

/**
 *
 * @author ferta
 */
public class Resumen implements AgentAction{
    private Cliente c ;
    public Resumen(){
        
    }
    
    public Resumen(Cliente c){
        this.c=c;
    
    }
    public Cliente getCliente(){
        return c;
    }
    public void setCliente(Cliente c){
        this.c=c;
    }
    
    
}
