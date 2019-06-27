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
package nyanclans.config;

import java.util.HashMap;
import java.util.Map;

/** @author NyanGuyMF - Vasiliy Bely */
public final class PlaceholdersBuilder {
    private Map<String, String> placeholders;

    public PlaceholdersBuilder() {
        placeholders = new HashMap<>();
    }

    /** Add "{%s}" placeholder associated with value. */
    public PlaceholdersBuilder add(final String placeholder, final String value) {
        placeholders.put("{" + placeholder + "}", value);
        return this;
    }

    public Map<String, String> build() {
        return placeholders;
    }
}
