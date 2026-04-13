package cl.hirata.view.model;

import cl.hirata.model.MaintenanceRecord;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class MaintenanceTableModel extends AbstractTableModel {

    private final String[] columns = {"ID", "Camion", "Fecha", "Tipo", "Km", "Observaciones"};
    private List<MaintenanceRecord> records = new ArrayList<>();

    public void setRecords(List<MaintenanceRecord> records) {
        this.records = new ArrayList<>(records);
        fireTableDataChanged();
    }

    public MaintenanceRecord getRecordAt(int rowIndex) {
        return records.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return records.size();
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
        MaintenanceRecord record = records.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> record.getId();
            case 1 -> record.getTruckDisplayName();
            case 2 -> record.getMaintenanceDate();
            case 3 -> record.getMaintenanceType();
            case 4 -> record.getMileageAtService();
            case 5 -> record.getObservations();
            default -> "";
        };
    }
}
