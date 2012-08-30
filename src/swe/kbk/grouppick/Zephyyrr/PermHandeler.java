package swe.kbk.grouppick.Zephyyrr;

import java.util.Collection;
import java.util.List;

public interface PermHandeler {

	public String findRace(String user, String world, List<String> races);

	/**
	 * This is the simple method to add a group to a player.
	 * 
	 * @param player The player to be modified
	 * @param group The group to be added
	 * @return if a change was made
	 */
	public boolean addGroup(String player, String world, String group);

	/**
	 * This is the simple method to remove a group from a player.
	 * 
	 * @param player The player to be modified
	 * @param group The group to be removed
	 * @return if a change was made
	 */
	public boolean rmGroup(String player, String world, String group);

	public boolean inGroup(String player, String world, String group);
	
	public List<String> getPlayerGroups(String player);
	
	public boolean promote(String player);
	
	public boolean demote(String player);
	
	public Collection<String> getGroups(String world);

	public boolean hasPermission(String name, String world, String perm);
	
	public String getGuestGroup();

}