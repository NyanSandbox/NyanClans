/*
 * This file is part of NyanClans Bukkit plug-in.
 *
 * NyanClans is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NyanClans is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NyanClans. If not, see <https://www.gnu.org/licenses/>.
 */
package nyanclans.db;

import static org.bukkit.configuration.file.YamlConfiguration.loadConfiguration;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Getter;

/** @author NyanGuyMF - Vasiliy Bely */
public final class DatabaseConfig {
    @Getter private File dataFolder;
    private YamlConfiguration config;
    private DatabaseDriver lastDriver;

    public DatabaseConfig(final File dataFolder) {
        this.dataFolder = dataFolder;
        config = loadConfiguration(new File(dataFolder, "database.yml"));
    }

    public synchronized void reload() {
        try {
            config.load(new File(dataFolder, "database.yml"));
        } catch (IOException | InvalidConfigurationException ex) {
            System.err.printf(
                "Unable to reload database.yml: %s", ex.getLocalizedMessage()
            );
        }
        lastDriver = null;
    }

    public synchronized String getUsername() {
        return config.getString("username", "root");
    }

    public synchronized void setUsername(final String username) {
        config.set("username", username);
    }

    public synchronized String getPassword() {
        return config.getString("password", "root");
    }

    public synchronized void setPassword(final String password) {
        config.set("password", password);
    }

    public synchronized String getDatabase() {
        return config.getString("database", "nyanclans");
    }

    public synchronized void setDatabase(final String database) {
        config.set("database", database);
    }

    public synchronized String getHost() {
        return getDriverName().equalsIgnoreCase("h2")
                ? new File(dataFolder, "h2-data").getAbsolutePath()
                : config.getString("host", "localhost");
    }

    public synchronized void setHost(final String host) {
        config.set("host", host);
    }

    public synchronized String getDriverName() {
        return config.getString("driver", "h2");
    }

    public synchronized void setDriverName(final String driverName) {
        config.set("driver", driverName);
    }

    public synchronized DatabaseDriver getDriver() {
        if (lastDriver != null)
            return lastDriver;

        for (DatabaseDriver driver : DatabaseDriver.values()) {
            if (driver.toString().equalsIgnoreCase(getDriverName())) {
                lastDriver = driver;
                return lastDriver;
            }
        }

        lastDriver = DatabaseDriver.H2;
        return lastDriver;
    }

    public synchronized int getPort() {
        return config.getInt("port", 3306);
    }

    public synchronized void setPort(final int port) {
        config.set("port", port);
    }
}
