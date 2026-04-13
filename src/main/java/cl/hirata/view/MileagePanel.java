package cl.hirata.view;

import cl.hirata.model.Truck;
import cl.hirata.model.TruckAlert;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MileagePanel extends JPanel {

    private final JComboBox<Truck> truckComboBox = new JComboBox<>();
    private final JTextField dateField = new JTextField(LocalDate.now().toString(), 10);
    private final JTextField kilometersField = new JTextField(10);
    private final JTextArea notesArea = new JTextArea(5, 30);
    private final JButton registerButton = new JButton("Registrar kilometraje");
    private final JLabel currentMileageValue = new JLabel("-");
    private final JLabel maintenanceStatusValue = new JLabel("-");

    public MileagePanel() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Camion"), gbc);
        gbc.gridx = 1;
        formPanel.add(truckComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Fecha viaje"), gbc);
        gbc.gridx = 1;
        formPanel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Km recorridos"), gbc);
        gbc.gridx = 1;
        formPanel.add(kilometersField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Notas"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(notesArea), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Kilometraje actual"), gbc);
        gbc.gridx = 1;
        formPanel.add(currentMileageValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Estado mantenimiento"), gbc);
        gbc.gridx = 1;
        formPanel.add(maintenanceStatusValue, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(registerButton, gbc);

        add(formPanel, BorderLayout.NORTH);
    }

    public JComboBox<Truck> getTruckComboBox() {
        return truckComboBox;
    }

    public JButton getRegisterButton() {
        return registerButton;
    }

    public String getDateText() {
        return dateField.getText().trim();
    }

    public String getKilometersText() {
        return kilometersField.getText().trim();
    }

    public String getNotesText() {
        return notesArea.getText().trim();
    }

    public Truck getSelectedTruck() {
        return (Truck) truckComboBox.getSelectedItem();
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

    public void updateTruckDetails(Truck truck, TruckAlert alert) {
        if (truck == null) {
            currentMileageValue.setText("-");
            maintenanceStatusValue.setText("-");
            return;
        }
        currentMileageValue.setText(truck.getCurrentMileage() + " km");
        maintenanceStatusValue.setText(alert == null ? "-" : alert.getMessage());
    }

    public void clearForm() {
        kilometersField.setText("");
        notesArea.setText("");
        dateField.setText(LocalDate.now().toString());
    }

    private GridBagConstraints createConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);
        return gbc;
    }
}
