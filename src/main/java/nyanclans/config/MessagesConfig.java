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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/** @author NyanGuyMF - Vasiliy Bely */
public final class MessagesConfig {
    private File messagesFile;
    private YamlConfiguration config;

    public MessagesConfig(final File messagesFile, final JavaPlugin plugin) {
        if (!messagesFile.exists()) {
            // save yaml file with comments
            try {
                Files.copy(plugin.getResource(messagesFile.getName()), messagesFile.toPath());
            } catch (IOException ex) {
                System.err.printf(
                    "Unable to save %s file: %s\n", messagesFile.getName(),
                    ex.getLocalizedMessage()
                );
                return;
            }
        }

        this.messagesFile = messagesFile;

        config = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public ConfigurationSection getSection(final String path) {
        return config.getConfigurationSection(path);
    }

    public String get(final String path, final String...replacement) {
        String message = config.getString(path);

        if (message == null)
            return message;

        for (short c = 0; c < replacement.length; c++)
            message.replace("{" + c + "}", replacement[c]);

        return message.replace('&', '\u00a7').replace("\\n", "\n");
    }

    public List<String> getList(final String path) {
        return config.getStringList(path);
    }

    public List<String> getList(final String path, final Map<String, String> placeholders) {
        List<String> messages = config.getStringList(path);

        if (messages == null)
            return messages;

        // It can be optimized in the future
        messages = messages.parallelStream().map(str -> {
            for (String placeholder : placeholders.keySet())
                str = str.replace(placeholder, placeholders.get(placeholder));

            return str.replace('&', '\u00a7').replace("\\n", "\n");
        }).collect(Collectors.toList());

        return messages;
    }

    public boolean reload() {
        try {
            config.load(messagesFile);
            return true;
        } catch (IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
