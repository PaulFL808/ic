package cl.hirata.controller;

import cl.hirata.model.Driver;
import cl.hirata.model.MaintenanceRecord;
import cl.hirata.model.Truck;
import cl.hirata.model.TruckAlert;
import cl.hirata.service.AlertService;
import cl.hirata.service.FleetService;
import cl.hirata.service.MaintenanceService;
import cl.hirata.service.MileageService;
import cl.hirata.view.FleetPanel;
import cl.hirata.view.MainFrame;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.event.ListSelectionEvent;

public class ApplicationController {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final MainFrame mainFrame;
    private final FleetService fleetService;
    private final MileageService mileageService;
    private final MaintenanceService maintenanceService;
    private final AlertService alertService;

    private List<Driver> currentDrivers = List.of();
    private List<Truck> currentTrucks = List.of();

    public ApplicationController(MainFrame mainFrame, FleetService fleetService, MileageService mileageService,
                                 MaintenanceService maintenanceService, AlertService alertService) {
        this.mainFrame = mainFrame;
        this.fleetService = fleetService;
        this.mileageService = mileageService;
        this.maintenanceService = maintenanceService;
        this.alertService = alertService;
    }

    public void initialize() {
        bindEvents();
        refreshAllData();
    }

    private void bindEvents() {
        mainFrame.getMileagePanel().getRegisterButton().addActionListener(event -> registerMileage());
        mainFrame.getMileagePanel().getTruckComboBox().addActionListener(event -> updateMileageStatus());

        FleetPanel fleetPanel = mainFrame.getFleetPanel();
        fleetPanel.getSaveDriverButton().addActionListener(event -> saveDriver());
        fleetPanel.getDeleteDriverButton().addActionListener(event -> deleteDriver());
        fleetPanel.getClearDriverButton().addActionListener(event -> fleetPanel.clearDriverForm());

        fleetPanel.getSaveTruckButton().addActionListener(event -> saveTruck());
        fleetPanel.getDeleteTruckButton().addActionListener(event -> deleteTruck());
        fleetPanel.getClearTruckButton().addActionListener(event -> fleetPanel.clearTruckForm());

        fleetPanel.getDriverTable().getSelectionModel().addListSelectionListener(this::handleDriverSelection);
        fleetPanel.getTruckTable().getSelectionModel().addListSelectionListener(this::handleTruckSelection);

        mainFrame.getMaintenancePanel().getSaveButton().addActionListener(event -> saveMaintenance());
        mainFrame.getMaintenancePanel().getDeleteButton().addActionListener(event -> deleteMaintenance());
        mainFrame.getMaintenancePanel().getClearButton().addActionListener(event -> mainFrame.getMaintenancePanel().clearForm());
        mainFrame.getMaintenancePanel().getMaintenanceTable().getSelectionModel()
                .addListSelectionListener(this::handleMaintenanceSelection);
    }

    private void refreshAllData() {
        currentDrivers = fleetService.listDrivers();
        currentTrucks = fleetService.listTrucks();
        List<MaintenanceRecord> maintenanceRecords = maintenanceService.listMaintenance();
        List<TruckAlert> alerts = alertService.generateAlerts(currentTrucks);

        mainFrame.getFleetPanel().setDrivers(currentDrivers);
        mainFrame.getFleetPanel().setDriverOptions(currentDrivers);
        mainFrame.getFleetPanel().setTrucks(currentTrucks);

        mainFrame.getMileagePanel().setTrucks(currentTrucks);
        mainFrame.getMaintenancePanel().setTrucks(currentTrucks);
        mainFrame.getMaintenancePanel().setRecords(maintenanceRecords);
        mainFrame.getDashboardPanel().updateSummary(currentTrucks.size(), alerts);

        updateMileageStatus();
    }

    private void saveDriver() {
        try {
            Driver selected = mainFrame.getFleetPanel().getSelectedDriverRow();
            Driver driver = new Driver(
                    selected == null ? null : selected.getId(),
                    mainFrame.getFleetPanel().getDriverNameText(),
                    mainFrame.getFleetPanel().getDriverLicenseText(),
                    mainFrame.getFleetPanel().getDriverPhoneText()
            );
            fleetService.saveDriver(driver);
            refreshAllData();
            mainFrame.getFleetPanel().clearDriverForm();
            mainFrame.showInfo("Conductor guardado correctamente.");
        } catch (Exception e) {
            mainFrame.showError(e.getMessage());
        }
    }

    private void deleteDriver() {
        try {
            Driver selected = mainFrame.getFleetPanel().getSelectedDriverRow();
            if (selected == null) {
                throw new IllegalArgumentException("Seleccione un conductor para eliminar.");
            }
            fleetService.deleteDriver(selected.getId());
            refreshAllData();
            mainFrame.getFleetPanel().clearDriverForm();
            mainFrame.showInfo("Conductor eliminado correctamente.");
        } catch (Exception e) {
            mainFrame.showError(e.getMessage());
        }
    }

    private void saveTruck() {
        try {
            Truck selected = mainFrame.getFleetPanel().getSelectedTruckRow();
            Driver assignedDriver = mainFrame.getFleetPanel().getSelectedTruckDriver();

            Truck truck = new Truck(
                    selected == null ? null : selected.getId(),
                    mainFrame.getFleetPanel().getTruckBrandText(),
                    mainFrame.getFleetPanel().getTruckModelText(),
                    parseInteger(mainFrame.getFleetPanel().getTruckYearText(), "El ano del camion debe ser numerico."),
                    assignedDriver == null || assignedDriver.getId() == null ? null : assignedDriver.getId(),
                    assignedDriver == null ? null : assignedDriver.getName(),
                    parseInteger(mainFrame.getFleetPanel().getTruckMileageText(), "El kilometraje debe ser numerico.")
            );
            fleetService.saveTruck(truck);
            refreshAllData();
            mainFrame.getFleetPanel().clearTruckForm();
            mainFrame.showInfo("Camion guardado correctamente.");
        } catch (Exception e) {
            mainFrame.showError(e.getMessage());
        }
    }

    private void deleteTruck() {
        try {
            Truck selected = mainFrame.getFleetPanel().getSelectedTruckRow();
            if (selected == null) {
                throw new IllegalArgumentException("Seleccione un camion para eliminar.");
            }
            fleetService.deleteTruck(selected.getId());
            refreshAllData();
            mainFrame.getFleetPanel().clearTruckForm();
            mainFrame.showInfo("Camion eliminado correctamente.");
        } catch (Exception e) {
            mainFrame.showError(e.getMessage());
        }
    }

    private void registerMileage() {
        try {
            Truck selectedTruck = mainFrame.getMileagePanel().getSelectedTruck();
            if (selectedTruck == null) {
                throw new IllegalArgumentException("Debe seleccionar un camion.");
            }

            mileageService.registerMileage(
                    selectedTruck.getId(),
                    parseInteger(mainFrame.getMileagePanel().getKilometersText(), "Los kilometros deben ser numericos."),
                    parseDate(mainFrame.getMileagePanel().getDateText()),
                    mainFrame.getMileagePanel().getNotesText()
            );
            refreshAllData();
            mainFrame.getMileagePanel().clearForm();
            mainFrame.showInfo("Kilometraje registrado correctamente.");
        } catch (Exception e) {
            mainFrame.showError(e.getMessage());
        }
    }

    private void saveMaintenance() {
        try {
            MaintenanceRecord selected = mainFrame.getMaintenancePanel().getSelectedRecord();
            Truck selectedTruck = mainFrame.getMaintenancePanel().getSelectedTruck();
            if (selectedTruck == null) {
                throw new IllegalArgumentException("Debe seleccionar un camion.");
            }

            MaintenanceRecord record = new MaintenanceRecord(
                    selected == null ? null : selected.getId(),
                    selectedTruck.getId(),
                    selectedTruck.getDisplayName(),
                    parseDate(mainFrame.getMaintenancePanel().getDateText()),
                    mainFrame.getMaintenancePanel().getTypeText(),
                    parseInteger(mainFrame.getMaintenancePanel().getMileageText(), "El kilometraje del mantenimiento debe ser numerico."),
                    mainFrame.getMaintenancePanel().getObservationsText()
            );
            maintenanceService.save(record);
            refreshAllData();
            mainFrame.getMaintenancePanel().clearForm();
            mainFrame.showInfo("Mantenimiento guardado correctamente.");
        } catch (Exception e) {
            mainFrame.showError(e.getMessage());
        }
    }

    private void deleteMaintenance() {
        try {
            MaintenanceRecord selected = mainFrame.getMaintenancePanel().getSelectedRecord();
            if (selected == null) {
                throw new IllegalArgumentException("Seleccione un mantenimiento para eliminar.");
            }
            maintenanceService.delete(selected.getId());
            refreshAllData();
            mainFrame.getMaintenancePanel().clearForm();
            mainFrame.showInfo("Mantenimiento eliminado correctamente.");
        } catch (Exception e) {
            mainFrame.showError(e.getMessage());
        }
    }

    private void updateMileageStatus() {
        Truck selectedTruck = mainFrame.getMileagePanel().getSelectedTruck();
        if (selectedTruck == null) {
            mainFrame.getMileagePanel().updateTruckDetails(null, null);
            return;
        }
        TruckAlert alert = alertService.buildAlert(selectedTruck);
        mainFrame.getMileagePanel().updateTruckDetails(selectedTruck, alert);
    }

    private void handleDriverSelection(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            Driver selected = mainFrame.getFleetPanel().getSelectedDriverRow();
            mainFrame.getFleetPanel().populateDriverForm(selected);
        }
    }

    private void handleTruckSelection(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            Truck selected = mainFrame.getFleetPanel().getSelectedTruckRow();
            mainFrame.getFleetPanel().populateTruckForm(selected, currentDrivers);
        }
    }

    private void handleMaintenanceSelection(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            mainFrame.getMaintenancePanel().populateForm(mainFrame.getMaintenancePanel().getSelectedRecord());
        }
    }

    private int parseInteger(String value, String errorMessage) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("La fecha debe tener formato yyyy-MM-dd.");
        }
    }
}
