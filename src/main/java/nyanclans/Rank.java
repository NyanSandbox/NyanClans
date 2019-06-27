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
import java.util.Arrays;
import java.util.List;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author NyanGuyMF - Vasiliy Bely */
@Data
@AllArgsConstructor
@NoArgsConstructor
@DatabaseTable(tableName="ranks")
public final class Rank {
    @DatabaseField(id=true)
    private int id;

    @DatabaseField
    private String displayName;

    @DatabaseField
    private String tag;

    @DatabaseField(foreign=true, foreignAutoCreate=false)
    private Clan owner;

    @DatabaseField(dataType=DataType.SERIALIZABLE)
    private List<String> permissions;

    public static Rank getLeaderRank(final Clan clan) {
        Rank rank = new Rank();

        rank.tag = "leader";
        rank.owner = clan;
        rank.displayName = "&6Leader";
        rank.permissions = Arrays.asList("all");

        return rank;
    }

    public static Rank getPlayerRank(final Clan clan) {
        Rank rank = new Rank();

        rank.tag = "player";
        rank.owner = clan;
        rank.displayName = "&7Player";
        rank.permissions = Arrays.asList("home", "pay", "chat");

        return rank;
    }

    public static Rank find(final int rankId) {
        try {
            return NyanClansPlugin.rankDao.queryForId(rankId);
        } catch (SQLException ex) {
            if (!NyanClansPlugin.reconnect()) {
                ex.printStackTrace();
                return null;
            } else
                return find(rankId);
        }
    }

    public boolean save() {
        try {
            NyanClansPlugin.rankDao.update(this);
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
            NyanClansPlugin.rankDao.create(this);
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
            NyanClansPlugin.rankDao.delete(this);
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
