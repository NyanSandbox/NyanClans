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
package nyanclans;

import static com.j256.ormlite.dao.DaoManager.createDao;

import java.io.File;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import nyanclans.commands.clan.ClanCommand;
import nyanclans.config.MessagesConfig;
import nyanclans.db.ConnectionCallback;
import nyanclans.db.DatabaseManager;

/** @author NyanGuyMF - Vasiliy Bely */
public final class NyanClansPlugin extends JavaPlugin {
    protected static Dao<ClanPlayer, String> playerDao;
    protected static Dao<Clan, String> clanDao;
    protected static Dao<Rank, Integer> rankDao;
    private static ConnectionCallback callback = conn -> {
        try {
            NyanClansPlugin.playerDao = createDao(conn, ClanPlayer.class);
            if (!NyanClansPlugin.playerDao.isTableExists())
                TableUtils.createTable(NyanClansPlugin.playerDao);
        } catch (SQLException ex) {
            System.err.printf(
                "Unable to create ClanPlayer dao: %s\n", ex.getLocalizedMessage()
            );
        }
        try {
            NyanClansPlugin.clanDao = createDao(conn, Clan.class);
            if (!NyanClansPlugin.clanDao.isTableExists())
                TableUtils.createTable(NyanClansPlugin.clanDao);
        } catch (SQLException ex) {
            System.err.printf(
                "Unable to create Clan dao: %s\n", ex.getLocalizedMessage()
            );
        }
        try {
            NyanClansPlugin.rankDao = createDao(conn, Rank.class);
            if (!NyanClansPlugin.rankDao.isTableExists())
                TableUtils.createTable(NyanClansPlugin.rankDao);
        } catch (SQLException ex) {
            System.err.printf(
                "Unable to create Rank dao: %s\n", ex.getLocalizedMessage()
            );
        }
    };
    private static DatabaseManager databaseManager;
    private MessagesConfig messagesConfig;

    @Override public void onLoad() {
        initializeFolder(getDataFolder());

        databaseManager = new DatabaseManager(getDataFolder());

        if (!databaseManager.connect(NyanClansPlugin.callback)) {
            System.err.println("Unable to connect to database.");
            System.out.println("Plugin will be disabled.");
        }

        messagesConfig = new MessagesConfig(
            new File(getDataFolder(), "messages.yml"), this
        );
    }

    @Override public void onEnable() {
        if (!databaseManager.isConnected()) {
            super.getPluginLoader().disablePlugin(this);
            return;
        }

        new ClanCommand(messagesConfig).register(this);
    }

    @Override public void onDisable() {
        databaseManager.close();
    }

    protected static boolean reconnect() {
        return databaseManager.reconnect(NyanClansPlugin.callback);
    }

    private void initializeFolder(final File dataFolder) {
        if (!dataFolder.exists())
            dataFolder.mkdir();

        File libsDir = new File(dataFolder, "libs");
        if (!libsDir.exists())
            libsDir.mkdir();
    }
}
