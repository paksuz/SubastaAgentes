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
public class Informar implements AgentAction {
   private Producto producto;
   
   public Informar(){
       
   }
    
   public Informar(Producto p){
       this.producto=p;
   }
   public Producto getProducto(){
       return producto;
   }
   public void setProducto(Producto p){
       this.producto = p;
   }
}
