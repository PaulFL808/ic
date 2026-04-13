package cl.hirata.view.model;

import cl.hirata.model.Driver;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class DriverTableModel extends AbstractTableModel {

    private final String[] columns = {"ID", "Nombre", "Licencia", "Telefono"};
    private List<Driver> drivers = new ArrayList<>();

    public void setDrivers(List<Driver> drivers) {
        this.drivers = new ArrayList<>(drivers);
        fireTableDataChanged();
    }

    public Driver getDriverAt(int rowIndex) {
        return drivers.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return drivers.size();
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
        Driver driver = drivers.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> driver.getId();
            case 1 -> driver.getName();
            case 2 -> driver.getLicenseNumber();
            case 3 -> driver.getPhone();
            default -> "";
        };
    }
}
