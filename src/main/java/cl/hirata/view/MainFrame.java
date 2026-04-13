package cl.hirata.view;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {

    private final DashboardPanel dashboardPanel = new DashboardPanel();
    private final MileagePanel mileagePanel = new MileagePanel();
    private final FleetPanel fleetPanel = new FleetPanel();
    private final MaintenancePanel maintenancePanel = new MaintenancePanel();

    public MainFrame() {
        setTitle("Hirata - Gestion de Flota");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 760);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Dashboard", dashboardPanel);
        tabbedPane.addTab("Kilometraje", mileagePanel);
        tabbedPane.addTab("Camiones y conductores", fleetPanel);
        tabbedPane.addTab("Mantenimientos", maintenancePanel);

        add(tabbedPane, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    public DashboardPanel getDashboardPanel() {
        return dashboardPanel;
    }

    public MileagePanel getMileagePanel() {
        return mileagePanel;
    }

    public FleetPanel getFleetPanel() {
        return fleetPanel;
    }

    public MaintenancePanel getMaintenancePanel() {
        return maintenancePanel;
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
