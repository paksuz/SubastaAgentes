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
public class Solicitud implements AgentAction {
    private Producto producto;
    
    public Solicitud(){
        
    }
    
    public Solicitud(Producto producto){
        this.producto = producto;
    }
    
    public Producto getProducto(){
        return producto;
    }
    
    public void setProducto(Producto p){
        this.producto = p;
    }
    
}
