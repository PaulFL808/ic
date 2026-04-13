package cl.hirata.app;

import cl.hirata.config.AppConfig;
import cl.hirata.controller.ApplicationController;
import cl.hirata.dao.DriverDao;
import cl.hirata.dao.MaintenanceDao;
import cl.hirata.dao.MileageLogDao;
import cl.hirata.dao.TruckDao;
import cl.hirata.dao.jdbc.JdbcDriverDao;
import cl.hirata.dao.jdbc.JdbcMaintenanceDao;
import cl.hirata.dao.jdbc.JdbcMileageLogDao;
import cl.hirata.dao.jdbc.JdbcTruckDao;
import cl.hirata.service.AlertService;
import cl.hirata.service.FleetService;
import cl.hirata.service.MaintenanceService;
import cl.hirata.service.MileageService;
import cl.hirata.util.DatabaseConnection;
import cl.hirata.view.MainFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppConfig appConfig = new AppConfig();
            DatabaseConnection databaseConnection = new DatabaseConnection(appConfig);

            DriverDao driverDao = new JdbcDriverDao(databaseConnection);
            TruckDao truckDao = new JdbcTruckDao(databaseConnection);
            MileageLogDao mileageLogDao = new JdbcMileageLogDao(databaseConnection);
            MaintenanceDao maintenanceDao = new JdbcMaintenanceDao(databaseConnection);

            FleetService fleetService = new FleetService(driverDao, truckDao);
            MileageService mileageService = new MileageService(truckDao, mileageLogDao);
            MaintenanceService maintenanceService = new MaintenanceService(maintenanceDao);
            AlertService alertService = new AlertService(maintenanceDao);

            MainFrame mainFrame = new MainFrame();
            ApplicationController controller = new ApplicationController(
                    mainFrame,
                    fleetService,
                    mileageService,
                    maintenanceService,
                    alertService
            );
            controller.initialize();
            mainFrame.setVisible(true);
        });
    }
}
