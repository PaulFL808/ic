package cl.hirata.view;

import cl.hirata.model.Driver;
import cl.hirata.model.Truck;
import cl.hirata.view.model.DriverTableModel;
import cl.hirata.view.model.TruckTableModel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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

public class FleetPanel extends JPanel {

    private final DriverTableModel driverTableModel = new DriverTableModel();
    private final JTable driverTable = new JTable(driverTableModel);
    private final JTextField driverNameField = new JTextField(16);
    private final JTextField driverLicenseField = new JTextField(16);
    private final JTextField driverPhoneField = new JTextField(16);
    private final JButton saveDriverButton = new JButton("Guardar conductor");
    private final JButton deleteDriverButton = new JButton("Eliminar conductor");
    private final JButton clearDriverButton = new JButton("Limpiar");

    private final TruckTableModel truckTableModel = new TruckTableModel();
    private final JTable truckTable = new JTable(truckTableModel);
    private final JTextField truckBrandField = new JTextField(16);
    private final JTextField truckModelField = new JTextField(16);
    private final JTextField truckYearField = new JTextField(16);
    private final JTextField truckMileageField = new JTextField(16);
    private final JComboBox<Driver> truckDriverComboBox = new JComboBox<>();
    private final JButton saveTruckButton = new JButton("Guardar camion");
    private final JButton deleteTruckButton = new JButton("Eliminar camion");
    private final JButton clearTruckButton = new JButton("Limpiar");

    public FleetPanel() {
        setLayout(new GridLayout(2, 1, 12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        driverTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        truckTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(buildDriverSection());
        add(buildTruckSection());
    }

    public JTable getDriverTable() {
        return driverTable;
    }

    public JTable getTruckTable() {
        return truckTable;
    }

    public JButton getSaveDriverButton() {
        return saveDriverButton;
    }

    public JButton getDeleteDriverButton() {
        return deleteDriverButton;
    }

    public JButton getClearDriverButton() {
        return clearDriverButton;
    }

    public JButton getSaveTruckButton() {
        return saveTruckButton;
    }

    public JButton getDeleteTruckButton() {
        return deleteTruckButton;
    }

    public JButton getClearTruckButton() {
        return clearTruckButton;
    }

    public Driver getSelectedDriverRow() {
        int row = driverTable.getSelectedRow();
        return row >= 0 ? driverTableModel.getDriverAt(row) : null;
    }

    public Truck getSelectedTruckRow() {
        int row = truckTable.getSelectedRow();
        return row >= 0 ? truckTableModel.getTruckAt(row) : null;
    }

    public String getDriverNameText() {
        return driverNameField.getText().trim();
    }

    public String getDriverLicenseText() {
        return driverLicenseField.getText().trim();
    }

    public String getDriverPhoneText() {
        return driverPhoneField.getText().trim();
    }

    public String getTruckBrandText() {
        return truckBrandField.getText().trim();
    }

    public String getTruckModelText() {
        return truckModelField.getText().trim();
    }

    public String getTruckYearText() {
        return truckYearField.getText().trim();
    }

    public String getTruckMileageText() {
        return truckMileageField.getText().trim();
    }

    public Driver getSelectedTruckDriver() {
        return (Driver) truckDriverComboBox.getSelectedItem();
    }

    public void setDrivers(List<Driver> drivers) {
        driverTableModel.setDrivers(drivers);
    }

    public void setTrucks(List<Truck> trucks) {
        truckTableModel.setTrucks(trucks);
    }

    public void setDriverOptions(List<Driver> drivers) {
        truckDriverComboBox.removeAllItems();
        truckDriverComboBox.addItem(new Driver(null, "Sin asignar", "", ""));
        for (Driver driver : drivers) {
            truckDriverComboBox.addItem(driver);
        }
    }

    public void populateDriverForm(Driver driver) {
        if (driver == null) {
            clearDriverForm();
            return;
        }
        driverNameField.setText(driver.getName());
        driverLicenseField.setText(driver.getLicenseNumber());
        driverPhoneField.setText(driver.getPhone());
    }

    public void populateTruckForm(Truck truck, List<Driver> drivers) {
        if (truck == null) {
            clearTruckForm();
            return;
        }
        truckBrandField.setText(truck.getBrand());
        truckModelField.setText(truck.getModel());
        truckYearField.setText(String.valueOf(truck.getManufactureYear()));
        truckMileageField.setText(String.valueOf(truck.getCurrentMileage()));

        if (truck.getAssignedDriverId() == null) {
            truckDriverComboBox.setSelectedIndex(0);
            return;
        }

        for (int i = 0; i < truckDriverComboBox.getItemCount(); i++) {
            Driver item = truckDriverComboBox.getItemAt(i);
            if (item != null && truck.getAssignedDriverId().equals(item.getId())) {
                truckDriverComboBox.setSelectedIndex(i);
                return;
            }
        }
        setDriverOptions(drivers);
    }

    public void clearDriverForm() {
        driverTable.clearSelection();
        driverNameField.setText("");
        driverLicenseField.setText("");
        driverPhoneField.setText("");
    }

    public void clearTruckForm() {
        truckTable.clearSelection();
        truckBrandField.setText("");
        truckModelField.setText("");
        truckYearField.setText("");
        truckMileageField.setText("0");
        if (truckDriverComboBox.getItemCount() > 0) {
            truckDriverComboBox.setSelectedIndex(0);
        }
    }

    private JPanel buildDriverSection() {
        JPanel section = new JPanel(new BorderLayout(12, 12));
        section.setBorder(BorderFactory.createTitledBorder("Conductores"));
        section.add(new JScrollPane(driverTable), BorderLayout.CENTER);
        section.add(buildDriverForm(), BorderLayout.EAST);
        return section;
    }

    private JPanel buildTruckSection() {
        JPanel section = new JPanel(new BorderLayout(12, 12));
        section.setBorder(BorderFactory.createTitledBorder("Camiones"));
        section.add(new JScrollPane(truckTable), BorderLayout.CENTER);
        section.add(buildTruckForm(), BorderLayout.EAST);
        return section;
    }

    private JPanel buildDriverForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createConstraints();

        addField(panel, gbc, 0, "Nombre", driverNameField);
        addField(panel, gbc, 1, "Licencia", driverLicenseField);
        addField(panel, gbc, 2, "Telefono", driverPhoneField);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(saveDriverButton, gbc);
        gbc.gridy = 4;
        panel.add(deleteDriverButton, gbc);
        gbc.gridy = 5;
        panel.add(clearDriverButton, gbc);
        return panel;
    }

    private JPanel buildTruckForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createConstraints();

        addField(panel, gbc, 0, "Marca", truckBrandField);
        addField(panel, gbc, 1, "Modelo", truckModelField);
        addField(panel, gbc, 2, "Ano", truckYearField);
        addField(panel, gbc, 3, "Kilometraje", truckMileageField);
        addField(panel, gbc, 4, "Conductor", truckDriverComboBox);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(saveTruckButton, gbc);
        gbc.gridy = 6;
        panel.add(deleteTruckButton, gbc);
        gbc.gridy = 7;
        panel.add(clearTruckButton, gbc);
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
