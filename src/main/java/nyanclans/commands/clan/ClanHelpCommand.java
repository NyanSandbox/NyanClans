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

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import nyanclans.ClanPlayer;
import nyanclans.commands.AbstractSubCommand;
import nyanclans.config.MessagesConfig;

/** @author NyanGuyMF - Vasiliy Bely */
public final class ClanHelpCommand extends AbstractSubCommand {
    private MessagesConfig messages;

    public ClanHelpCommand(final MessagesConfig messagesConfig) {
        super("help");

        messages = messagesConfig;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final String[] args) {
        ClanPlayer player = ClanPlayer.find(sender.getName());

        if (!player.isClanMember()) {
            for (String message : messages.getList("clan.help-not-member"))
                sender.sendMessage(message);

            return true;
        }

        if (player.getRank().getPermissions().contains("all")) {
            ConfigurationSection messagesSection = messages.getSection(
                "clan.help-member"
            );

            for (String key : messagesSection.getKeys(false))
                sender.sendMessage(messagesSection.getString(key));

            for (String defaultMessage : messages.getList("clan.help-default"))
                sender.sendMessage(defaultMessage);

            return true;
        }

        for (String permission : player.getRank().getPermissions()) {
            String message = messages.get("clan.help-member." + permission);

            if (message != null)
                sender.sendMessage(message);
        }

        for (String defaultMessage : messages.getList("clan.help-default"))
            sender.sendMessage(defaultMessage);

        return true;
    }
}
