package cl.hirata.view.model;

import cl.hirata.model.TruckAlert;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class AlertTableModel extends AbstractTableModel {

    private final String[] columns = {"Camion", "Km actual", "Ult. mantencion", "Km desde mantencion", "Estado"};
    private List<TruckAlert> alerts = new ArrayList<>();

    public void setAlerts(List<TruckAlert> alerts) {
        this.alerts = new ArrayList<>(alerts);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return alerts.size();
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
        TruckAlert alert = alerts.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> alert.getTruckDisplayName();
            case 1 -> alert.getCurrentMileage();
            case 2 -> alert.getLastMaintenanceMileage();
            case 3 -> alert.getKilometersSinceMaintenance();
            case 4 -> alert.getMessage();
            default -> "";
        };
    }
}
