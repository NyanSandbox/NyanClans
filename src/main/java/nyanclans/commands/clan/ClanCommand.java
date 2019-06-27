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
package nyanclans.commands.clan;

import org.bukkit.plugin.java.JavaPlugin;

import nyanclans.commands.CommandManager;
import nyanclans.config.MessagesConfig;

/** @author NyanGuyMF - Vasiliy Bely */
public final class ClanCommand extends CommandManager {
    public ClanCommand(final MessagesConfig messagesConfig) {
        super("clan");
        super.addSubCommand(new ClanHelpCommand(messagesConfig));
    }

    public void register(final JavaPlugin plugin) {
        plugin.getCommand(getName()).setExecutor(this);
    }
}
