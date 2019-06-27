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

import static java.lang.String.format;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

import org.apache.commons.codec.digest.DigestUtils;

import lombok.AllArgsConstructor;
import lombok.Data;

/** @author NyanGuyMF - Vasiliy Bely */
@Data
@AllArgsConstructor
class DatabaseDriverManager {
    private DatabaseConfig config;

    public synchronized boolean installDriver() {
        if (!isDriverExists())
            if (!downloadDriver())
                return false;

        if (!isDriverValid()) {
            getDriverFile().delete();

            if (!downloadDriver())
                return false;

            if (!isDriverValid())
                return false;
        }

        return loadDriver();
    }

    public synchronized String generateUrl() {
        String url = config.getDriver().getConnUrl();

        switch (config.getDriver()) {
        case H2:
            url = format(url, config.getHost());
            break;
        case MYSQL:
            url = format(
                url, config.getHost(), config.getPort(),
                config.getDatabase()
            );
            break;
        }

        return url;
    }

    public synchronized boolean isDriverLoaded() {
        try {
            Class.forName(config.getDriver().getClassPath());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public synchronized boolean isDriverExists() {
        return getDriverFile().exists();
    }

    public synchronized boolean loadDriver() {
        return new ReflectionClassLoader(this).loadJar(
            getDriverFile().toPath()
        );
    }

    public synchronized boolean downloadDriver() {
        File driverFile = getDriverFile();
        try {
            driverFile.createNewFile();
        } catch (IOException ex) {
            System.err.printf(
                "Unable to create file for driver: %s\n", ex.getLocalizedMessage()
            );
            return false;
        }

        URL downloadUrl = null;

        try {
            downloadUrl = new URL(config.getDriver().getDownloadUrl());
        } catch (MalformedURLException ex) {
            System.err.printf(
                "Invalid download driver url: %s\n", ex.getLocalizedMessage()
            );
            return false;
        }

        URLConnection conn = null;

        try {
            conn = downloadUrl.openConnection();
            conn.connect();
        } catch (IOException ex) {
            System.err.printf(
                "Unable to establish connection: %s\n", ex.getLocalizedMessage()
            );
            return false;
        }

        try {
            Files.copy(
                conn.getInputStream(), driverFile.toPath(), REPLACE_EXISTING
            );
        } catch (IOException ex) {
            System.err.printf(
                "Error while downloading driver: %s\n", ex.getLocalizedMessage()
            );
            ex.printStackTrace();
            return false;
        }

        try {
            conn.getInputStream().close();
            conn.getOutputStream().close();
        } catch (IOException ignore) {}

        return true;
    }

    public synchronized boolean compareMd5(final File file, final String hash) {
        try (InputStream in = new FileInputStream(file)){
            return DigestUtils.md5Hex(in).equals(hash);
        } catch (IOException ignore) {}

        return false;
    }

    private synchronized File getDriverFile() {
        // i.e. "~/server/plugins/NyanClans/libs/h2.jar"
        return new File(
            config.getDataFolder().getAbsolutePath() + File.separatorChar
            + "libs" + File.separatorChar + config.getDriver().toString() + ".jar"
        );
    }

    private boolean isDriverValid() {
        return compareMd5(
            getDriverFile(), config.getDriver().getMd5hash()
        );
    }
}
