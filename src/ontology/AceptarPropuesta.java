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
public class AceptarPropuesta implements AgentAction {
    private Producto producto;
    
    public AceptarPropuesta(){
        
    }
    
    public AceptarPropuesta(Producto producto){
        this.producto = producto;
    }
    
    public Producto getProducto(){
        return producto;
    }
    public void setProducto(Producto producto){
        this.producto=producto;
    }
}
