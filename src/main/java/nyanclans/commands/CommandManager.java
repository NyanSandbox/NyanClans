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

import lombok.AccessLevel;
import lombok.Getter;

/** @author NyanGuyMF - Vasiliy Bely */
public abstract class CommandManager implements CommandExecutor {
    @Getter private String name;
    @Getter(value=AccessLevel.PROTECTED)
    private CommandMessages messages;
    @Getter(value=AccessLevel.PROTECTED)
    private List<SubCommand> subCommands;

    public CommandManager(final String name, final CommandMessages messages) {
        this.name = name;
        this.messages = messages;
        subCommands = new ArrayList<>();
    }
    @Override public boolean onCommand(
        final CommandSender sender, final Command command,
        final String label, final String[] args
    ) {
        if (args.length == 0) {
            sender.sendMessage(messages.getUsage(getName()));
            return true;
        }

        String subAlias = args[0];
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        for (SubCommand sub : subCommands) {
            if (sub.getName().equalsIgnoreCase(subAlias)) {
                executeSubCommand(sender, sub, subArgs);
                return true;
            }
        }

        sender.sendMessage(messages.getNotFoundMessage(subAlias));
        return true;
    }

    private void executeSubCommand(
        final CommandSender sender, final SubCommand sub,
        final String[] args
    ) {
        if (!sub.hasPermission(sender))
            sender.sendMessage(messages.getNoPermission(sub.getName()));
        else if (!sub.onCommand(sender, args))
            sender.sendMessage(messages.getUsage(sub.getName()));
    }

    protected void addSubCommand(final SubCommand subCommand) {
        subCommands.add(subCommand);
    }
}
