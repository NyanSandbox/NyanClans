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

import lombok.AllArgsConstructor;
import lombok.Getter;

/** @author NyanGuyMF - Vasiliy Bely */
@AllArgsConstructor
public enum DatabaseDriver {
    MYSQL(
        "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.16/mysql-connector-java-8.0.16.jar",
        "3ff7fbacc1ec1a34537e977d6359ff2b", "com.mysql.jdbc.Driver",
        "jdbc:mysql://%s:%d/%s"
    ),
    H2(
        "http://repo1.maven.org/maven2/com/h2database/h2/1.4.199/h2-1.4.199.jar",
        "f805f57d838de4b42ce01c7f85e46e1c", "org.h2.Driver",
        "jdbc:h2:%s"
    );

    @Getter private final String downloadUrl;
    @Getter private final String md5hash;
    @Getter private final String classPath;
    @Getter private final String connUrl;
}
