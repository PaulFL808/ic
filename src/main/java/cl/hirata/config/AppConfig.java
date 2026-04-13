package cl.hirata.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private final Properties properties = new Properties();

    public AppConfig() {
        try (InputStream inputStream = AppConfig.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (inputStream == null) {
                throw new IllegalStateException("No se encontro el archivo database.properties en resources.");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("No fue posible cargar la configuracion de base de datos.", e);
        }
    }

    public String getDbUrl() {
        return properties.getProperty("db.url");
    }

    public String getDbUsername() {
        return properties.getProperty("db.username");
    }

    public String getDbPassword() {
        return properties.getProperty("db.password");
    }
}
