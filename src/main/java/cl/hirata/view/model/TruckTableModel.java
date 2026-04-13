package cl.hirata.view.model;

import cl.hirata.model.Truck;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TruckTableModel extends AbstractTableModel {

    private final String[] columns = {"ID", "Marca", "Modelo", "Ano", "Kilometraje", "Conductor"};
    private List<Truck> trucks = new ArrayList<>();

    public void setTrucks(List<Truck> trucks) {
        this.trucks = new ArrayList<>(trucks);
        fireTableDataChanged();
    }

    public Truck getTruckAt(int rowIndex) {
        return trucks.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return trucks.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Truck truck = trucks.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> truck.getId();
            case 1 -> truck.getBrand();
            case 2 -> truck.getModel();
            case 3 -> truck.getManufactureYear();
            case 4 -> truck.getCurrentMileage();
            case 5 -> truck.getAssignedDriverName() == null ? "Sin asignar" : truck.getAssignedDriverName();
            default -> "";
        };
    }
}
