package cl.hirata.view;

import cl.hirata.model.TruckAlert;
import cl.hirata.view.model.AlertTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

public class DashboardPanel extends JPanel {

    private final JLabel totalTrucksLabel = new JLabel("0", SwingConstants.CENTER);
    private final JLabel activeAlertsLabel = new JLabel("0", SwingConstants.CENTER);
    private final JLabel healthyTrucksLabel = new JLabel("0", SwingConstants.CENTER);
    private final AlertTableModel alertTableModel = new AlertTableModel();

    public DashboardPanel() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 12, 12));
        summaryPanel.add(createSummaryCard("Camiones registrados", totalTrucksLabel, new Color(230, 240, 255)));
        summaryPanel.add(createSummaryCard("Alertas activas", activeAlertsLabel, new Color(255, 228, 225)));
        summaryPanel.add(createSummaryCard("Operativos", healthyTrucksLabel, new Color(231, 247, 234)));

        JTable alertTable = new JTable(alertTableModel);
        alertTable.setFillsViewportHeight(true);

        add(summaryPanel, BorderLayout.NORTH);
        add(new JScrollPane(alertTable), BorderLayout.CENTER);
    }

    public void updateSummary(int totalTrucks, List<TruckAlert> alerts) {
        totalTrucksLabel.setText(String.valueOf(totalTrucks));
        activeAlertsLabel.setText(String.valueOf(alerts.size()));
        healthyTrucksLabel.setText(String.valueOf(Math.max(0, totalTrucks - alerts.size())));
        alertTableModel.setAlerts(alerts);
    }

    private JPanel createSummaryCard(String title, JLabel valueLabel, Color background) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        panel.setBackground(background);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        valueLabel.setFont(valueLabel.getFont().deriveFont(26f));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }
}
