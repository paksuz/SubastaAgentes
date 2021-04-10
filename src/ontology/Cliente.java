/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ontology;

import jade.core.AID;

/**
 *
 * @author ferta
 */
public class Cliente {
    
    private String nombre;
    private int wallet;
    public Cliente(){
        
    }
    public Cliente(String nombre, int wallet){
        this.nombre=nombre;
        this.wallet=wallet;
    }
    
    public String getNombre(){
        return nombre;
    }
    public int getWallet(){
        return wallet;
    }
    public void setWallet(int w){
        this.wallet=w;
    }
    public void setNombre(String s){
        this.nombre=s;
    }
    
    
    
    
}
