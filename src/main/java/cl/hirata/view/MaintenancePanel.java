package cl.hirata.view;

import cl.hirata.model.MaintenanceRecord;
import cl.hirata.model.Truck;
import cl.hirata.view.model.MaintenanceTableModel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class MaintenancePanel extends JPanel {

    private final MaintenanceTableModel maintenanceTableModel = new MaintenanceTableModel();
    private final JTable maintenanceTable = new JTable(maintenanceTableModel);
    private final JComboBox<Truck> truckComboBox = new JComboBox<>();
    private final JTextField dateField = new JTextField(16);
    private final JTextField typeField = new JTextField(16);
    private final JTextField mileageField = new JTextField(16);
    private final JTextField observationsField = new JTextField(16);
    private final JButton saveButton = new JButton("Guardar mantenimiento");
    private final JButton deleteButton = new JButton("Eliminar mantenimiento");
    private final JButton clearButton = new JButton("Limpiar");

    public MaintenancePanel() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        maintenanceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(maintenanceTable), BorderLayout.CENTER);
        add(buildFormPanel(), BorderLayout.EAST);
    }

    public JTable getMaintenanceTable() {
        return maintenanceTable;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public Truck getSelectedTruck() {
        return (Truck) truckComboBox.getSelectedItem();
    }

    public String getDateText() {
        return dateField.getText().trim();
    }

    public String getTypeText() {
        return typeField.getText().trim();
    }

    public String getMileageText() {
        return mileageField.getText().trim();
    }

    public String getObservationsText() {
        return observationsField.getText().trim();
    }

    public MaintenanceRecord getSelectedRecord() {
        int row = maintenanceTable.getSelectedRow();
        return row >= 0 ? maintenanceTableModel.getRecordAt(row) : null;
    }

    public void setTrucks(List<Truck> trucks) {
        truckComboBox.removeAllItems();
        for (Truck truck : trucks) {
            truckComboBox.addItem(truck);
        }
        if (truckComboBox.getItemCount() > 0) {
            truckComboBox.setSelectedIndex(0);
        }
    }

    public void setRecords(List<MaintenanceRecord> records) {
        maintenanceTableModel.setRecords(records);
    }

    public void populateForm(MaintenanceRecord record) {
        if (record == null) {
            clearForm();
            return;
        }

        dateField.setText(record.getMaintenanceDate().toString());
        typeField.setText(record.getMaintenanceType());
        mileageField.setText(String.valueOf(record.getMileageAtService()));
        observationsField.setText(record.getObservations());

        for (int i = 0; i < truckComboBox.getItemCount(); i++) {
            Truck truck = truckComboBox.getItemAt(i);
            if (truck != null && record.getTruckId().equals(truck.getId())) {
                truckComboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    public void clearForm() {
        maintenanceTable.clearSelection();
        dateField.setText("");
        typeField.setText("");
        mileageField.setText("");
        observationsField.setText("");
        if (truckComboBox.getItemCount() > 0) {
            truckComboBox.setSelectedIndex(0);
        }
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Formulario"));
        GridBagConstraints gbc = createConstraints();

        addField(panel, gbc, 0, "Camion", truckComboBox);
        addField(panel, gbc, 1, "Fecha", dateField);
        addField(panel, gbc, 2, "Tipo", typeField);
        addField(panel, gbc, 3, "Km servicio", mileageField);
        addField(panel, gbc, 4, "Observaciones", observationsField);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(saveButton, gbc);
        gbc.gridy = 6;
        panel.add(deleteButton, gbc);
        gbc.gridy = 7;
        panel.add(clearButton, gbc);
        return panel;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, java.awt.Component component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private GridBagConstraints createConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);
        return gbc;
    }
}
