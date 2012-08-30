package swe.kbk.grouppick.Zephyyrr.Handelers;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import swe.kbk.grouppick.Zephyyrr.GroupPick;
import swe.kbk.grouppick.Zephyyrr.PermHandeler;

public class AdminHandeler implements CommandExecutor {

	Logger log;
	GroupPick plugin;
	PermHandeler ph;

	public AdminHandeler(GroupPick grouppick) {
		log = Logger.getLogger("Minecraft.bukkit.GroupPick");
		plugin = grouppick;
		ph = grouppick.getPermissionHandeler();
		if (plugin == null) {
			log.severe("AdminHandeler: PLUGIN IS NULL!");
		}
		if (ph == null) {
			log.severe("AdminHandeler: PERMISSIONHANDELER IS NULL!");
		}

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (hasPermission(sender, "groupick.admin")) {
			return adminmode(sender, args);
		}
		return false;
	}

	public boolean hasPermission(CommandSender user, String perm) {
		return (user instanceof Player && ((Player) user).hasPermission(perm) || user instanceof ConsoleCommandSender);
	}

	private boolean adminmode(CommandSender sender, String[] args) {
		sender.sendMessage("You have access to this command!");

		if (args.length >= 3) {

			String target = args[2];

			if (args[1].equalsIgnoreCase("add")
					|| args[1].equalsIgnoreCase("a")) {
				if (target != null && plugin.groupExists(args[2])) {
					ph.addGroup(target, "", args[2]);
					return true;
				} else {
					if (target == null) {
						sender.sendMessage("Could not find target!");
					}
					if (plugin.groupExists(args[2])) {
						sender.sendMessage("The group does not exist!");
					}
					return false;
				}
			} else if (args[1].equalsIgnoreCase("remove")
					|| args[1].equalsIgnoreCase("r")) {
				if (target != null && plugin.groupExists(args[2])) {
					ph.rmGroup(target, "", args[2]);
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		return false;
	}

}
