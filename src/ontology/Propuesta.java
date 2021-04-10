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
public class Propuesta implements AgentAction {
    private boolean answer;
    private Producto producto;
    
    public Propuesta(){
        
    }
    
    public Propuesta(Boolean answer, Producto producto){
        this.answer=answer;
        this.producto=producto;
        
    }
    
    public Producto getProducto(){
        return producto;
    }
    public boolean getAnswer(){
        return answer;
    }
    public void setAnswer(boolean a){
        this.answer=a;
    }
    public void setProducto(Producto b){
        this.producto=b;
    }
    

}

