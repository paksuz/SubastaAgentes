package subastador;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import ontology.Producto;

@SuppressWarnings("serial")
public class ModeloTabla extends AbstractTableModel {

	public ArrayList<Producto> productos;

	public ModeloTabla(ArrayList<Producto> productos) {
		this.productos = productos;
		fireTableDataChanged();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return super.getColumnClass(columnIndex);
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int column) {
		String tmp = "";
		switch (column) {
		case 0:
			tmp = "Nombre";
			break;
		case 1:
			tmp = "Precio";
			break;
		case 2:
			tmp = "Ganador";
			break;
		}
		return tmp;
	}

	@Override
	public int getRowCount() {
		return productos.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return productos.get(rowIndex).getNombre();
		case 1:
			return productos.get(rowIndex).getPrecio();
		case 2:
			return productos.get(rowIndex).getGanador();
		}
		return "default";
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return super.isCellEditable(rowIndex, columnIndex);
	}

	public void changeStatus(Producto producto) {
		int i = 0;
		int temp = 0;
		for (Producto b : productos) {
			if (b.equals(producto)) {
				temp = i;
			}
			i++;
		}
		productos.set(temp,producto);
                
		fireTableDataChanged();
	}
}