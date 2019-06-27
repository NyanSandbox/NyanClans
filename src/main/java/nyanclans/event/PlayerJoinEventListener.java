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
package nyanclans.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.AllArgsConstructor;
import nyanclans.ClanPlayer;

/** @author NyanGuyMF - Vasiliy Bely */
@AllArgsConstructor
public final class PlayerJoinEventListener implements Listener {
    private JavaPlugin plugin;

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        ClanPlayer player = ClanPlayer.find(event.getPlayer().getName());

        if (player == null) {
            player = new ClanPlayer(event.getPlayer().getName());
            player.create();
        }

        if (player.isClanMember()) {
            plugin.getServer().getPluginManager().callEvent(
                new ClanPlayerJoinEvent(player, event)
            );
        }
    }

    public PlayerJoinEventListener register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        return this;
    }
}
