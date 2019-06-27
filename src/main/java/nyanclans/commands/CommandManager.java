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
package nyanclans.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import lombok.Getter;

/** @author NyanGuyMF - Vasiliy Bely */
public abstract class CommandManager implements CommandExecutor {
    @Getter private String name;
    private List<SubCommand> subCommands;

    public CommandManager(final String name) {
        this.name = name;
        subCommands = new ArrayList<>();
    }

    @Override public boolean onCommand(
        final CommandSender sender, final Command command,
        final String label, final String[] args
    ) {
        if (args.length == 0) {
            sendUsage(getName(), sender);
            return true;
        }

        String subAlias = args[0];
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        for (SubCommand sub : subCommands) {
            if (sub.getName().equalsIgnoreCase(subAlias)) {
                if (!sub.hasPermission(sender))
                    sendNoPermission(sub.getName(), sender);
                else if (!sub.onCommand(sender, subArgs))
                    sendUsage(sub.getName(), sender);

                break;
            }
        }

        sendNotFound(subAlias, sender);

        return true;
    }

    public void sendNotFound(final String command, final CommandSender sender) {
        sender.sendMessage(String.format(
            "&cSub command «&6%s&c» not found.", command
        ).replace('&', '\u00a7'));
    }

    public void sendUsage(final String command, final CommandSender sender) {
        sender.sendMessage(String.format(
            "&cWrong usage of «&6%s&c» command.", command
        ).replace('&', '\u00a7'));
    }

    public void sendNoPermission(final String command, final CommandSender sender) {
        sender.sendMessage(String.format(
            "&cYou have no permission to perform «&6%s&c» command.", command
        ).replace('&', '\u00a7'));
    }

    protected void addSubCommand(final SubCommand subCommand) {

    }
}
