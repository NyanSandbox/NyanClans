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

import java.sql.SQLException;

import javax.persistence.Table;

import com.j256.ormlite.field.DatabaseField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author NyanGuyMF - Vasiliy Bely */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="players")
public final class ClanPlayer {
    @DatabaseField(id=true)
    private String name;

    @DatabaseField(foreign=true, foreignAutoCreate=false)
    private Clan clan;

    @DatabaseField(foreign=true, foreignAutoCreate=false)
    private Rank rank;

    public ClanPlayer(String name) {
        this.name = name;
    }

    public boolean isClanMember() {
        return clan != null;
    }

    public static ClanPlayer find(final String playerName) {
        try {
            return NyanClansPlugin.playerDao.queryForId(playerName);
        } catch (SQLException ex) {
            if (!NyanClansPlugin.reconnect()) {
                ex.printStackTrace();
                return null;
            } else
                return find(playerName);
        }
    }

    public boolean save() {
        try {
            NyanClansPlugin.playerDao.update(this);
            return true;
        } catch (SQLException ex) {
            if (!NyanClansPlugin.reconnect()) {
                ex.printStackTrace();
                return false;
            } else
                return save();
        }
    }

    public boolean create() {
        try {
            NyanClansPlugin.playerDao.create(this);
            return true;
        } catch (SQLException ex) {
            if (!NyanClansPlugin.reconnect()) {
                ex.printStackTrace();
                return false;
            } else
                return create();
        }
    }

    public boolean delete() {
        try {
            NyanClansPlugin.playerDao.delete(this);
            return true;
        } catch (SQLException ex) {
            if (!NyanClansPlugin.reconnect()) {
                ex.printStackTrace();
                return false;
            } else
                return delete();
        }
    }
}
