package ontology;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ferta
 */
public class Producto {

    private String nombre;
    private String marca;
    private int precio;
    private String ganador;
    private double incremento;
    private boolean flag;
    private int factor;
    private String estado;
    
    private int factorCosto = 2;

    public Producto() {

    }

    public Producto(String nombre, String marca, int precio) {
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.incremento = 1.2;
    }

    public Producto(String nombre, String marca, int precio, int factor) {
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio*factorCosto;
        this.incremento = 2;
        this.factor = factor;
        this.estado="";
    }

    
    public Producto(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getFactor() {
        return factor;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getMarca() {
        return marca;
    }

    public void setPrecio(int precio) {
        this.precio = precio;

    }

    public void setIncremento(int i) {
        this.incremento = i;
    }

    public int getPrecio() {
        return precio;
    }

    public boolean getFlag() {
        return flag;
    }

    public void actualizarPrecio() {

        setPrecio(((int) (precio * incremento)));
        this.flag = true;
    }

    public String getGanador() {
        return ganador;
    }

    public double getIncremento() {
        return incremento;

    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

    @Override
    public String toString() {
        return "Producto [nombre=" + nombre + ", marca=" + marca + ", precio=" + precio + "]";
    }

}
