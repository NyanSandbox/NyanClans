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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

import com.google.common.collect.ImmutableMap;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author NyanGuyMF - Vasiliy Bely */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="clan")
public final class Clan {
    @DatabaseField(id=true)
    private String name;

    @DatabaseField(foreign=true, foreignAutoCreate=false)
    private ClanPlayer leader;

    @ForeignCollectionField(foreignFieldName="owner")
    private Collection<Rank> ranks;

    public static Clan find(final String clanName) {
        try {
            return NyanClansPlugin.clanDao.queryForId(clanName);
        } catch (SQLException ex) {
            if (!NyanClansPlugin.reconnect()) {
                ex.printStackTrace();
                return null;
            }
            return find(clanName);
        }
    }

    public boolean save() {
        try {
            NyanClansPlugin.clanDao.update(this);
            return true;
        } catch (SQLException ex) {
            if (!NyanClansPlugin.reconnect()) {
                ex.printStackTrace();
                return false;
            }
            return save();
        }
    }

    public boolean create() {
        try {
            NyanClansPlugin.clanDao.create(this);
            return true;
        } catch (SQLException ex) {
            if (!NyanClansPlugin.reconnect()) {
                ex.printStackTrace();
                return false;
            }
            return create();
        }
    }

    public boolean delete() {
        try {
            NyanClansPlugin.clanDao.delete(this);
            return true;
        } catch (SQLException ex) {
            if (!NyanClansPlugin.reconnect()) {
                ex.printStackTrace();
                return false;
            }
            return delete();
        }
    }

    public List<ClanPlayer> getMembers() {
        Map<String, Object> fields = ImmutableMap.<String, Object>builder()
            .put("clan_id", this)
            .build();

        try {
            return NyanClansPlugin.playerDao.queryForFieldValues(fields);
        } catch (SQLException ex) {
            if (!NyanClansPlugin.reconnect()) {
                ex.printStackTrace();
                return null;
            }
            return getMembers();
        }
    }
    
    public boolean addMember(ClanPlayer newMember) {
        if (newMember.isClanMember())
            return false;

        newMember.setClan(this);
        return true;
    }
}
