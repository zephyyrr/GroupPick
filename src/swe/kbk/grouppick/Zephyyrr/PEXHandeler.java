package swe.kbk.grouppick.Zephyyrr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import ru.tehkode.permissions.exceptions.RankingException;

public class PEXHandeler implements PermHandeler {

	Logger logger;
	PermissionManager pm;

	public PEXHandeler(Logger logger) {
		this.logger = logger;

		pm = PermissionsEx.getPermissionManager();
	}

	@Override
	public String findRace(String player, String world, List<String> races) {
		for (String race : races) {
			if (inGroup(player, world, race)) {
				return race;
			}
		}
		return null;
	}

	@Override
	public boolean addGroup(String player, String world, String group) {
		if (pm.getGroup(group) != null) {
			pm.getUser(player).addGroup(group);
			return true;
		}
		return false;

	}

	@Override
	public boolean rmGroup(String player, String world, String group) {
		pm.getUser(player).removeGroup(group,world);
		return (inGroup(player, world, group));

	}

	@Override
	public boolean inGroup(String player, String world, String group) {
		return pm.getUser(player).inGroup(group);
	}

	@Override
	public List<String> getPlayerGroups(String player) {
		String[] sa = pm.getUser(player).getGroupsNames();
		List<String> l = new LinkedList<String>();

		for (String s : sa) {
			l.add(s);
		}

		return l;
	}

	@Override
	public Collection<String> getGroups(String world) {
		PermissionGroup[] groupsArray = pm.getGroups();
		ArrayList<String> groups = new ArrayList<String>();

		for (int i = 0; i < groupsArray.length; i++) {
			groups.add(groupsArray[i].getName());
		}

		return groups;
	}

	@Override
	public boolean hasPermission(String name, String world, String perm) {
		return pm.has(name, perm, world);
	}

	public String toString() {
		return "PermissionsEX";
	}

	@Override
	public boolean promote(String player) {
		try {
			pm.getUser(player).promote(null, null);
		} catch (RankingException e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean demote(String player) {
		// TODO Implement demote for PEXHandeler
		return false;
	}

	@Override
	public String getGuestGroup() {
		System.out.println(pm.getDefaultGroup().getName());
		return pm.getDefaultGroup().getName();
	}

}
