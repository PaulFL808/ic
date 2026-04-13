CREATE DATABASE IF NOT EXISTS hirata_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hirata_db;

CREATE TABLE IF NOT EXISTS drivers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    license_number VARCHAR(40) NOT NULL,
    phone VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS trucks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(80) NOT NULL,
    model VARCHAR(80) NOT NULL,
    manufacture_year INT NOT NULL,
    assigned_driver_id INT NULL,
    current_mileage INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_truck_driver
        FOREIGN KEY (assigned_driver_id) REFERENCES drivers(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS mileage_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    truck_id INT NOT NULL,
    trip_date DATE NOT NULL,
    kilometers_travelled INT NOT NULL,
    total_mileage_after_trip INT NOT NULL,
    notes VARCHAR(255),
    CONSTRAINT fk_mileage_truck
        FOREIGN KEY (truck_id) REFERENCES trucks(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS maintenance_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    truck_id INT NOT NULL,
    maintenance_date DATE NOT NULL,
    maintenance_type VARCHAR(120) NOT NULL,
    mileage_at_service INT NOT NULL,
    observations VARCHAR(255),
    CONSTRAINT fk_maintenance_truck
        FOREIGN KEY (truck_id) REFERENCES trucks(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

INSERT INTO drivers (name, license_number, phone)
SELECT * FROM (
    SELECT 'Carlos Soto', 'A-101-CHL', '+56 9 1111 2222'
) AS seed
WHERE NOT EXISTS (SELECT 1 FROM drivers WHERE license_number = 'A-101-CHL');

INSERT INTO drivers (name, license_number, phone)
SELECT * FROM (
    SELECT 'Mariela Rojas', 'B-202-CHL', '+56 9 3333 4444'
) AS seed
WHERE NOT EXISTS (SELECT 1 FROM drivers WHERE license_number = 'B-202-CHL');

INSERT INTO trucks (brand, model, manufacture_year, assigned_driver_id, current_mileage)
SELECT * FROM (
    SELECT 'Volvo', 'FH16', 2022, 1, 4800
) AS seed
WHERE NOT EXISTS (SELECT 1 FROM trucks WHERE brand = 'Volvo' AND model = 'FH16' AND manufacture_year = 2022);

INSERT INTO trucks (brand, model, manufacture_year, assigned_driver_id, current_mileage)
SELECT * FROM (
    SELECT 'Scania', 'R450', 2021, 2, 6100
) AS seed
WHERE NOT EXISTS (SELECT 1 FROM trucks WHERE brand = 'Scania' AND model = 'R450' AND manufacture_year = 2021);

INSERT INTO maintenance_records (truck_id, maintenance_date, maintenance_type, mileage_at_service, observations)
SELECT * FROM (
    SELECT 1, '2026-03-10', 'Cambio de aceite', 1200, 'Mantenimiento preventivo inicial'
) AS seed
WHERE NOT EXISTS (
    SELECT 1
    FROM maintenance_records
    WHERE truck_id = 1
      AND maintenance_date = '2026-03-10'
      AND mileage_at_service = 1200
);
