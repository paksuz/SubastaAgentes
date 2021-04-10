/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienteAfectivo;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;
import ontology.Producto;

/**
 *
 * @author ferta
 */
public class ModeloTabla extends AbstractTableModel {
    
    private HashMap<Producto, String> productos;

	public ModeloTabla(HashMap<Producto, String> productos) {
		this.productos = productos;
		fireTableDataChanged();
	}
        @Override
	public Class<?> getColumnClass(int columnIndex) {
		return super.getColumnClass(columnIndex);
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int column) {
		String tmp = "";
		switch(column) {
		case 0:
			tmp = "Nombre";
			break;
		case 1:
			tmp = "Precio Maximo";
			break;
                case 2:
                        tmp = "Estado";
                
                case 3: tmp ="Factor Somatico";
		}
		return tmp;
	}

	@Override
	public int getRowCount() {
		return productos.size();
	}


	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ArrayList <Producto> tmp = new ArrayList<>(productos.keySet());
		switch(columnIndex){
		case 0:
			return tmp.get(rowIndex).getNombre();
		case 1:
			return tmp.get(rowIndex).getPrecio();
                case 2:
                    
                        return tmp.get(rowIndex).getEstado();
                
                case 3: return tmp.get(rowIndex).getFactor();
                        
		}
		return "default";
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return super.isCellEditable(rowIndex, columnIndex);
	}

	
        
       public void changeStatus(Producto producto) {
	String aux = producto.getNombre();
        for(Producto p : productos.keySet()){
            if(p.getNombre().equals(aux)){
                p=producto;
            }
        }
        fireTableDataChanged();
          
	}

        
    
}
