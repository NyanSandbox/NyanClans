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

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

/** @author NyanGuyMF - Vasiliy Bely */
public final class DatabaseManager {
    private DatabaseDriverManager driverManager;
    private DatabaseConfig config;
    private ConnectionSource conn;

    public DatabaseManager(final File dataFolder) {
        config = new DatabaseConfig(dataFolder);

        driverManager = new DatabaseDriverManager(config);
    }

    public synchronized boolean connect(final ConnectionCallback callback) {
        if (!driverManager.isDriverLoaded())
            if (!driverManager.installDriver())
                return false;

        String connUrl = driverManager.generateUrl();
        try {
            conn = new JdbcConnectionSource(
                connUrl, config.getUsername(), config.getPassword()
            );
        } catch (SQLException ex) {
            System.err.printf("Unable to connect: %s\n", ex.getLocalizedMessage());
            return false;
        }

        callback.run(conn);

        return true;
    }

    public synchronized boolean reconnect(final ConnectionCallback callback) {
        close();
        return connect(callback);
    }

    public synchronized boolean isConnected() {
        return conn != null;
    }

    public synchronized void close() {
        try {
            conn.close();
        } catch (IOException ignore) {}
    }
}
